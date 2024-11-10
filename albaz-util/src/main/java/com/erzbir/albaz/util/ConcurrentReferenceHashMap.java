package com.erzbir.albaz.util;

import org.jetbrains.annotations.Nullable;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentReferenceHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;

    private static final ReferenceType DEFAULT_REFERENCE_TYPE = ReferenceType.SOFT;

    private static final int MAXIMUM_CONCURRENCY_LEVEL = 1 << 16;

    private static final int MAXIMUM_SEGMENT_SIZE = 1 << 30;

    private final Segment[] segments;

    private final float loadFactor;

    private final ReferenceType referenceType;

    private final int shift;

    @Nullable
    private volatile Set<Map.Entry<K, V>> entrySet;


    public ConcurrentReferenceHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, DEFAULT_CONCURRENCY_LEVEL, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, DEFAULT_CONCURRENCY_LEVEL, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, DEFAULT_CONCURRENCY_LEVEL, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, int concurrencyLevel) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, ReferenceType referenceType) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, DEFAULT_CONCURRENCY_LEVEL, referenceType);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        this(initialCapacity, loadFactor, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
    }

    @SuppressWarnings("unchecked")
    public ConcurrentReferenceHashMap(
            int initialCapacity, float loadFactor, int concurrencyLevel, ReferenceType referenceType) {

        this.loadFactor = loadFactor;
        this.shift = calculateShift(concurrencyLevel, MAXIMUM_CONCURRENCY_LEVEL);
        int size = 1 << this.shift;
        this.referenceType = referenceType;
        int roundedUpSegmentCapacity = (int) ((initialCapacity + size - 1L) / size);
        int initialSize = 1 << calculateShift(roundedUpSegmentCapacity, MAXIMUM_SEGMENT_SIZE);
        Segment[] segments = (Segment[]) Array.newInstance(Segment.class, size);
        int resizeThreshold = (int) (initialSize * getLoadFactor());
        for (int i = 0; i < segments.length; i++) {
            segments[i] = new Segment(initialSize, resizeThreshold);
        }
        this.segments = segments;
    }

    protected static int calculateShift(int minimumValue, int maximumValue) {
        int shift = 0;
        int value = 1;
        while (value < minimumValue && value < maximumValue) {
            value <<= 1;
            shift++;
        }
        return shift;
    }

    protected final float getLoadFactor() {
        return this.loadFactor;
    }

    protected final int getSegmentsSize() {
        return this.segments.length;
    }

    protected final Segment getSegment(int index) {
        return this.segments[index];
    }

    protected ReferenceManager createReferenceManager() {
        return new ReferenceManager();
    }

    protected int getHash(@Nullable Object o) {
        int hash = (o != null ? o.hashCode() : 0);
        hash += (hash << 15) ^ 0xffffcd7d;
        hash ^= (hash >>> 10);
        hash += (hash << 3);
        hash ^= (hash >>> 6);
        hash += (hash << 2) + (hash << 14);
        hash ^= (hash >>> 16);
        return hash;
    }

    @Override
    @Nullable
    public V get(@Nullable Object key) {
        Reference<K, V> ref = getReference(key, Restructure.WHEN_NECESSARY);
        Entry<K, V> entry = (ref != null ? ref.get() : null);
        return (entry != null ? entry.getValue() : null);
    }

    @Override
    @Nullable
    public V getOrDefault(@Nullable Object key, @Nullable V defaultValue) {
        Reference<K, V> ref = getReference(key, Restructure.WHEN_NECESSARY);
        Entry<K, V> entry = (ref != null ? ref.get() : null);
        return (entry != null ? entry.getValue() : defaultValue);
    }

    @Override
    public boolean containsKey(@Nullable Object key) {
        Reference<K, V> ref = getReference(key, Restructure.WHEN_NECESSARY);
        Entry<K, V> entry = (ref != null ? ref.get() : null);
        return (entry != null && ObjectUtils.nullSafeEquals(entry.getKey(), key));
    }

    /**
     * Return a {@link Reference} to the {@link Entry} for the specified {@code key},
     * or {@code null} if not found.
     *
     * @param key         the key (can be {@code null})
     * @param restructure types of restructure allowed during this call
     * @return the reference, or {@code null} if not found
     */
    @Nullable
    protected final Reference<K, V> getReference(@Nullable Object key, Restructure restructure) {
        int hash = getHash(key);
        return getSegmentForHash(hash).getReference(key, hash, restructure);
    }

    @Override
    @Nullable
    public V put(@Nullable K key, @Nullable V value) {
        return put(key, value, true);
    }

    @Override
    @Nullable
    public V putIfAbsent(@Nullable K key, @Nullable V value) {
        return put(key, value, false);
    }

    @Nullable
    private V put(@Nullable final K key, @Nullable final V value, final boolean overwriteExisting) {
        return doTask(key, new Task<V>(TaskOption.RESTRUCTURE_BEFORE, TaskOption.RESIZE) {
            @Override
            @Nullable
            protected V execute(@Nullable Reference<K, V> ref, @Nullable Entry<K, V> entry, @Nullable Entries<V> entries) {
                if (entry != null) {
                    V oldValue = entry.getValue();
                    if (overwriteExisting) {
                        entry.setValue(value);
                    }
                    return oldValue;
                }
                entries.add(value);
                return null;
            }
        });
    }

    @Override
    @Nullable
    public V remove(@Nullable Object key) {
        return doTask(key, new Task<V>(TaskOption.RESTRUCTURE_AFTER, TaskOption.SKIP_IF_EMPTY) {
            @Override
            @Nullable
            protected V execute(@Nullable Reference<K, V> ref, @Nullable Entry<K, V> entry) {
                if (entry != null) {
                    if (ref != null) {
                        ref.release();
                    }
                    return entry.value;
                }
                return null;
            }
        });
    }

    @Override
    public boolean remove(@Nullable Object key, @Nullable final Object value) {
        Boolean result = doTask(key, new Task<Boolean>(TaskOption.RESTRUCTURE_AFTER, TaskOption.SKIP_IF_EMPTY) {
            @Override
            protected Boolean execute(@Nullable Reference<K, V> ref, @Nullable Entry<K, V> entry) {
                if (entry != null && ObjectUtils.nullSafeEquals(entry.getValue(), value)) {
                    if (ref != null) {
                        ref.release();
                    }
                    return true;
                }
                return false;
            }
        });
        return (Boolean.TRUE.equals(result));
    }

    @Override
    public boolean replace(@Nullable K key, @Nullable final V oldValue, @Nullable final V newValue) {
        Boolean result = doTask(key, new Task<Boolean>(TaskOption.RESTRUCTURE_BEFORE, TaskOption.SKIP_IF_EMPTY) {
            @Override
            protected Boolean execute(@Nullable Reference<K, V> ref, @Nullable Entry<K, V> entry) {
                if (entry != null && ObjectUtils.nullSafeEquals(entry.getValue(), oldValue)) {
                    entry.setValue(newValue);
                    return true;
                }
                return false;
            }
        });
        return (Boolean.TRUE.equals(result));
    }

    @Override
    @Nullable
    public V replace(@Nullable K key, @Nullable final V value) {
        return doTask(key, new Task<V>(TaskOption.RESTRUCTURE_BEFORE, TaskOption.SKIP_IF_EMPTY) {
            @Override
            @Nullable
            protected V execute(@Nullable Reference<K, V> ref, @Nullable Entry<K, V> entry) {
                if (entry != null) {
                    V oldValue = entry.getValue();
                    entry.setValue(value);
                    return oldValue;
                }
                return null;
            }
        });
    }

    @Override
    public void clear() {
        for (Segment segment : this.segments) {
            segment.clear();
        }
    }

    public void purgeUnreferencedEntries() {
        for (Segment segment : this.segments) {
            segment.restructureIfNecessary(false);
        }
    }

    @Override
    public int size() {
        int size = 0;
        for (Segment segment : this.segments) {
            size += segment.getCount();
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        for (Segment segment : this.segments) {
            if (segment.getCount() > 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entrySet = this.entrySet;
        if (entrySet == null) {
            entrySet = new EntrySet();
            this.entrySet = entrySet;
        }
        return entrySet;
    }

    @Nullable
    private <T> T doTask(@Nullable Object key, Task<T> task) {
        int hash = getHash(key);
        return getSegmentForHash(hash).doTask(hash, key, task);
    }

    private Segment getSegmentForHash(int hash) {
        return this.segments[(hash >>> (32 - this.shift)) & (this.segments.length - 1)];
    }


    /**
     * Various reference types supported by this map.
     */
    public enum ReferenceType {

        /**
         * Use {@link SoftReference SoftReferences}.
         */
        SOFT,

        /**
         * Use {@link WeakReference WeakReferences}.
         */
        WEAK
    }


    /**
     * Various options supported by a {@code Task}.
     */
    private enum TaskOption {

        RESTRUCTURE_BEFORE, RESTRUCTURE_AFTER, SKIP_IF_EMPTY, RESIZE
    }


    /**
     * The types of restructuring that can be performed.
     */
    protected enum Restructure {

        WHEN_NECESSARY, NEVER
    }


    /**
     * A reference to an {@link Entry} contained in the map. Implementations are usually
     * wrappers around specific Java reference implementations (e.g., {@link SoftReference}).
     *
     * @param <K> the key type
     * @param <V> the value type
     */
    protected interface Reference<K, V> {

        /**
         * Return the referenced entry, or {@code null} if the entry is no longer available.
         */
        @Nullable
        Entry<K, V> get();

        /**
         * Return the hash for the reference.
         */
        int getHash();

        /**
         * Return the next reference in the chain, or {@code null} if none.
         */
        @Nullable
        Reference<K, V> getNext();

        /**
         * Release this entry and ensure that it will be returned from
         * {@code ReferenceManager#pollForPurge()}.
         */
        void release();
    }


    /**
     * Allows a task access to {@link Segment} entries.
     */
    private interface Entries<V> {

        /**
         * Add a new entry with the specified value.
         *
         * @param value the value to add
         */
        void add(@Nullable V value);
    }

    /**
     * A single map entry.
     *
     * @param <K> the key type
     * @param <V> the value type
     */
    protected static final class Entry<K, V> implements Map.Entry<K, V> {

        @Nullable
        private final K key;

        @Nullable
        private volatile V value;

        public Entry(@Nullable K key, @Nullable V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        @Nullable
        public K getKey() {
            return this.key;
        }

        @Override
        @Nullable
        public V getValue() {
            return this.value;
        }

        @Override
        @Nullable
        public V setValue(@Nullable V value) {
            V previous = this.value;
            this.value = value;
            return previous;
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return (this == other || (other instanceof Map.Entry<?, ?> that &&
                    ObjectUtils.nullSafeEquals(getKey(), that.getKey()) &&
                    ObjectUtils.nullSafeEquals(getValue(), that.getValue())));
        }

        @Override
        public int hashCode() {
            return (ObjectUtils.nullSafeHashCode(this.key) ^ ObjectUtils.nullSafeHashCode(this.value));
        }

        @Override
        public String toString() {
            return (this.key + "=" + this.value);
        }
    }

    /**
     * Internal {@link Reference} implementation for {@link SoftReference SoftReferences}.
     */
    private static final class SoftEntryReference<K, V> extends SoftReference<Entry<K, V>> implements Reference<K, V> {

        private final int hash;

        @Nullable
        private final Reference<K, V> nextReference;

        public SoftEntryReference(Entry<K, V> entry, int hash, @Nullable Reference<K, V> next,
                                  ReferenceQueue<Entry<K, V>> queue) {

            super(entry, queue);
            this.hash = hash;
            this.nextReference = next;
        }

        @Override
        public int getHash() {
            return this.hash;
        }

        @Override
        @Nullable
        public Reference<K, V> getNext() {
            return this.nextReference;
        }

        @Override
        public void release() {
            enqueue();
        }
    }

    /**
     * Internal {@link Reference} implementation for {@link WeakReference WeakReferences}.
     */
    private static final class WeakEntryReference<K, V> extends WeakReference<Entry<K, V>> implements Reference<K, V> {

        private final int hash;

        @Nullable
        private final Reference<K, V> nextReference;

        public WeakEntryReference(Entry<K, V> entry, int hash, @Nullable Reference<K, V> next,
                                  ReferenceQueue<Entry<K, V>> queue) {

            super(entry, queue);
            this.hash = hash;
            this.nextReference = next;
        }

        @Override
        public int getHash() {
            return this.hash;
        }

        @Override
        @Nullable
        public Reference<K, V> getNext() {
            return this.nextReference;
        }

        @Override
        public void release() {
            enqueue();
        }
    }

    @SuppressWarnings("serial")
    protected final class Segment extends ReentrantLock {

        private final ReferenceManager referenceManager;

        private final int initialSize;
        private final AtomicInteger count = new AtomicInteger();
        private volatile Reference<K, V>[] references;
        private int resizeThreshold;

        public Segment(int initialSize, int resizeThreshold) {
            this.referenceManager = createReferenceManager();
            this.initialSize = initialSize;
            this.references = createReferenceArray(initialSize);
            this.resizeThreshold = resizeThreshold;
        }

        @Nullable
        public Reference<K, V> getReference(@Nullable Object key, int hash, Restructure restructure) {
            if (restructure == Restructure.WHEN_NECESSARY) {
                restructureIfNecessary(false);
            }
            if (this.count.get() == 0) {
                return null;
            }
            // Use a local copy to protect against other threads writing
            Reference<K, V>[] references = this.references;
            int index = getIndex(hash, references);
            Reference<K, V> head = references[index];
            return findInChain(head, key, hash);
        }

        /**
         * Apply an update operation to this segment.
         * The segment will be locked during the update.
         *
         * @param hash the hash of the key
         * @param key  the key
         * @param task the update operation
         * @return the result of the operation
         */
        @Nullable
        public <T> T doTask(final int hash, @Nullable final Object key, final Task<T> task) {
            boolean resize = task.hasOption(TaskOption.RESIZE);
            if (task.hasOption(TaskOption.RESTRUCTURE_BEFORE)) {
                restructureIfNecessary(resize);
            }
            if (task.hasOption(TaskOption.SKIP_IF_EMPTY) && this.count.get() == 0) {
                return task.execute(null, null, null);
            }
            lock();
            try {
                final int index = getIndex(hash, this.references);
                final Reference<K, V> head = this.references[index];
                Reference<K, V> ref = findInChain(head, key, hash);
                Entry<K, V> entry = (ref != null ? ref.get() : null);
                Entries<V> entries = value -> {
                    @SuppressWarnings("unchecked")
                    Entry<K, V> newEntry = new Entry<>((K) key, value);
                    Reference<K, V> newReference = Segment.this.referenceManager.createReference(newEntry, hash, head);
                    Segment.this.references[index] = newReference;
                    Segment.this.count.incrementAndGet();
                };
                return task.execute(ref, entry, entries);
            } finally {
                unlock();
                if (task.hasOption(TaskOption.RESTRUCTURE_AFTER)) {
                    restructureIfNecessary(resize);
                }
            }
        }

        /**
         * Clear all items from this segment.
         */
        public void clear() {
            if (this.count.get() == 0) {
                return;
            }
            lock();
            try {
                this.references = createReferenceArray(this.initialSize);
                this.resizeThreshold = (int) (this.references.length * getLoadFactor());
                this.count.set(0);
            } finally {
                unlock();
            }
        }

        /**
         * Restructure the underlying data structure when it becomes necessary. This
         * method can increase the size of the references table as well as purge any
         * references that have been garbage collected.
         *
         * @param allowResize if resizing is permitted
         */
        void restructureIfNecessary(boolean allowResize) {
            int currCount = this.count.get();
            boolean needsResize = allowResize && (currCount > 0 && currCount >= this.resizeThreshold);
            Reference<K, V> ref = this.referenceManager.pollForPurge();
            if (ref != null || (needsResize)) {
                restructure(allowResize, ref);
            }
        }

        private void restructure(boolean allowResize, @Nullable Reference<K, V> ref) {
            boolean needsResize;
            lock();
            try {
                int expectedCount = this.count.get();
                Set<Reference<K, V>> toPurge = Collections.emptySet();
                if (ref != null) {
                    toPurge = new HashSet<>();
                    while (ref != null) {
                        toPurge.add(ref);
                        ref = this.referenceManager.pollForPurge();
                    }
                }
                expectedCount -= toPurge.size();

                // Estimate new count, taking into account count inside lock and items that
                // will be purged.
                needsResize = (expectedCount > 0 && expectedCount >= this.resizeThreshold);
                boolean resizing = false;
                int restructureSize = this.references.length;
                if (allowResize && needsResize && restructureSize < MAXIMUM_SEGMENT_SIZE) {
                    restructureSize <<= 1;
                    resizing = true;
                }

                int newCount = 0;
                // Restructure the resized reference array
                if (resizing) {
                    Reference<K, V>[] restructured = createReferenceArray(restructureSize);
                    for (Reference<K, V> reference : this.references) {
                        ref = reference;
                        while (ref != null) {
                            if (!toPurge.contains(ref)) {
                                Entry<K, V> entry = ref.get();
                                // Also filter out null references that are now null
                                // they should be polled from the queue in a later restructure call.
                                if (entry != null) {
                                    int index = getIndex(ref.getHash(), restructured);
                                    restructured[index] = this.referenceManager.createReference(
                                            entry, ref.getHash(), restructured[index]);
                                    newCount++;
                                }
                            }
                            ref = ref.getNext();
                        }
                    }
                    // Replace volatile members
                    this.references = restructured;
                    this.resizeThreshold = (int) (this.references.length * getLoadFactor());
                }
                // Restructure the existing reference array "in place"
                else {
                    for (int i = 0; i < this.references.length; i++) {
                        Reference<K, V> purgedRef = null;
                        ref = this.references[i];
                        while (ref != null) {
                            if (!toPurge.contains(ref)) {
                                Entry<K, V> entry = ref.get();
                                // Also filter out null references that are now null
                                // they should be polled from the queue in a later restructure call.
                                if (entry != null) {
                                    purgedRef = this.referenceManager.createReference(
                                            entry, ref.getHash(), purgedRef);
                                }
                                newCount++;
                            }
                            ref = ref.getNext();
                        }
                        this.references[i] = purgedRef;
                    }
                }
                this.count.set(Math.max(newCount, 0));
            } finally {
                unlock();
            }
        }

        @Nullable
        private Reference<K, V> findInChain(Reference<K, V> ref, @Nullable Object key, int hash) {
            Reference<K, V> currRef = ref;
            while (currRef != null) {
                if (currRef.getHash() == hash) {
                    Entry<K, V> entry = currRef.get();
                    if (entry != null) {
                        K entryKey = entry.getKey();
                        if (ObjectUtils.nullSafeEquals(entryKey, key)) {
                            return currRef;
                        }
                    }
                }
                currRef = currRef.getNext();
            }
            return null;
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        private Reference<K, V>[] createReferenceArray(int size) {
            return new Reference[size];
        }

        private int getIndex(int hash, Reference<K, V>[] references) {
            return (hash & (references.length - 1));
        }

        /**
         * Return the size of the current references array.
         */
        public int getSize() {
            return this.references.length;
        }

        /**
         * Return the total number of references in this segment.
         */
        public int getCount() {
            return this.count.get();
        }
    }

    /**
     * A task that can be {@link Segment#doTask run} against a {@link Segment}.
     */
    private abstract class Task<T> {

        private final EnumSet<TaskOption> options;

        public Task(TaskOption... options) {
            this.options = (options.length == 0 ? EnumSet.noneOf(TaskOption.class) : EnumSet.of(options[0], options));
        }

        public boolean hasOption(TaskOption option) {
            return this.options.contains(option);
        }

        /**
         * Execute the task.
         *
         * @param ref     the found reference (or {@code null})
         * @param entry   the found entry (or {@code null})
         * @param entries access to the underlying entries
         * @return the result of the task
         * @see #execute(Reference, Entry)
         */
        @Nullable
        protected T execute(@Nullable Reference<K, V> ref, @Nullable Entry<K, V> entry, @Nullable Entries<V> entries) {
            return execute(ref, entry);
        }

        /**
         * Convenience method that can be used for tasks that do not need access to {@link Entries}.
         *
         * @param ref   the found reference (or {@code null})
         * @param entry the found entry (or {@code null})
         * @return the result of the task
         * @see #execute(Reference, Entry, Entries)
         */
        @Nullable
        protected T execute(@Nullable Reference<K, V> ref, @Nullable Entry<K, V> entry) {
            return null;
        }
    }

    /**
     * Internal entry-set implementation.
     */
    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public boolean contains(@Nullable Object o) {
            if (o instanceof Map.Entry<?, ?> entry) {
                Reference<K, V> ref = ConcurrentReferenceHashMap.this.getReference(entry.getKey(), Restructure.NEVER);
                Entry<K, V> otherEntry = (ref != null ? ref.get() : null);
                if (otherEntry != null) {
                    return ObjectUtils.nullSafeEquals(entry.getValue(), otherEntry.getValue());
                }
            }
            return false;
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof Map.Entry<?, ?> entry) {
                return ConcurrentReferenceHashMap.this.remove(entry.getKey(), entry.getValue());
            }
            return false;
        }

        @Override
        public int size() {
            return ConcurrentReferenceHashMap.this.size();
        }

        @Override
        public void clear() {
            ConcurrentReferenceHashMap.this.clear();
        }
    }

    /**
     * Internal entry iterator implementation.
     */
    private class EntryIterator implements Iterator<Map.Entry<K, V>> {

        private int segmentIndex;

        private int referenceIndex;

        @Nullable
        private Reference<K, V>[] references;

        @Nullable
        private Reference<K, V> reference;

        @Nullable
        private Entry<K, V> next;

        @Nullable
        private Entry<K, V> last;

        public EntryIterator() {
            moveToNextSegment();
        }

        @Override
        public boolean hasNext() {
            getNextIfNecessary();
            return (this.next != null);
        }

        @Override
        public Entry<K, V> next() {
            getNextIfNecessary();
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            this.last = this.next;
            this.next = null;
            return this.last;
        }

        private void getNextIfNecessary() {
            while (this.next == null) {
                moveToNextReference();
                if (this.reference == null) {
                    return;
                }
                this.next = this.reference.get();
            }
        }

        private void moveToNextReference() {
            if (this.reference != null) {
                this.reference = this.reference.getNext();
            }
            while (this.reference == null && this.references != null) {
                if (this.referenceIndex >= this.references.length) {
                    moveToNextSegment();
                    this.referenceIndex = 0;
                } else {
                    this.reference = this.references[this.referenceIndex];
                    this.referenceIndex++;
                }
            }
        }

        private void moveToNextSegment() {
            this.reference = null;
            this.references = null;
            if (this.segmentIndex < ConcurrentReferenceHashMap.this.segments.length) {
                this.references = ConcurrentReferenceHashMap.this.segments[this.segmentIndex].references;
                this.segmentIndex++;
            }
        }

        @Override
        public void remove() {
            ConcurrentReferenceHashMap.this.remove(this.last.getKey());
            this.last = null;
        }
    }

    /**
     * Strategy class used to manage {@link Reference References}.
     * This class can be overridden if alternative reference types need to be supported.
     */
    protected class ReferenceManager {

        private final ReferenceQueue<Entry<K, V>> queue = new ReferenceQueue<>();

        /**
         * Factory method used to create a new {@link Reference}.
         *
         * @param entry the entry contained in the reference
         * @param hash  the hash
         * @param next  the next reference in the chain, or {@code null} if none
         * @return a new {@link Reference}
         */
        public Reference<K, V> createReference(Entry<K, V> entry, int hash, @Nullable Reference<K, V> next) {
            if (ConcurrentReferenceHashMap.this.referenceType == ReferenceType.WEAK) {
                return new WeakEntryReference<>(entry, hash, next, this.queue);
            }
            return new SoftEntryReference<>(entry, hash, next, this.queue);
        }

        /**
         * Return any reference that has been garbage collected and can be purged from the
         * underlying structure or {@code null} if no references need purging. This
         * method must be thread safe and ideally should not block when returning
         * {@code null}. References should be returned once and only once.
         *
         * @return a reference to purge or {@code null}
         */
        @SuppressWarnings("unchecked")
        @Nullable
        public Reference<K, V> pollForPurge() {
            return (Reference<K, V>) this.queue.poll();
        }
    }

}
