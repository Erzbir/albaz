package com.erzbir.albaz.util;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class ClassScanner {

    private String basePackage; // 主包
    private String packageDirName;
    private Predicate<Class<?>> classPredicate; // 用于过滤
    private ClassLoader classLoader;
    private Set<Class<?>> classes = new HashSet<>();  // 扫瞄后将字节码放到这个Set
    private Set<String> classesOfLoadError = new HashSet<>();
    private boolean initialize;
    private boolean ignoreLoadError;

    /**
     * @param basePackage    主包
     * @param classPredicate 用于过滤
     * @param classLoader    类加载器
     */
    public ClassScanner(String basePackage, ClassLoader classLoader,
                        Predicate<Class<?>> classPredicate) {
        if (basePackage.endsWith(".")) {
            basePackage = basePackage.substring(0, basePackage.lastIndexOf('.'));
        }
        this.basePackage = basePackage;
        this.packageDirName = basePackage.replace('.', '/');
        this.classPredicate = classPredicate;
        this.classLoader = classLoader;
        this.initialize = false;
        this.ignoreLoadError = false;
    }

    public ClassScanner(String basePackage, ClassLoader classLoader) {
        this(basePackage, classLoader, null);
    }

    public ClassScanner(String basePackage) {
        this(basePackage, Thread.currentThread().getContextClassLoader(), null);
    }

    public ClassScanner(String basePackage, Predicate<Class<?>> classPredicate) {
        this(basePackage, Thread.currentThread().getContextClassLoader(), classPredicate);
    }

    public static Set<Class<?>> scanAllPackageByAnnotation(String packageName, Class<? extends Annotation> annotationClass) {
        return scanAllPackage(packageName, clazz -> clazz.isAnnotationPresent(annotationClass));
    }

    public static Set<Class<?>> scanPackageByAnnotation(String packageName, Class<? extends Annotation> annotationClass) {
        return scanPackage(packageName, clazz -> clazz.isAnnotationPresent(annotationClass));
    }

    public static Set<Class<?>> scanAllPackageBySuper(String packageName, Class<?> superClass) {
        return scanAllPackage(packageName, clazz -> superClass.isAssignableFrom(clazz) && !superClass.equals(clazz));
    }

    public static Set<Class<?>> scanPackageBySuper(String packageName, Class<?> superClass) {
        return scanPackage(packageName, clazz -> superClass.isAssignableFrom(clazz) && !superClass.equals(clazz));
    }

    public static Set<Class<?>> scanPackage() {
        return scanPackage("", (Predicate<Class<?>>) null);
    }

    public static Set<Class<?>> scanPackage(String packageName) {
        return scanPackage(packageName, (Predicate<Class<?>>) null);
    }

    public static Set<Class<?>> scanPackage(String packageName, ClassLoader classLoader, boolean force) {
        return new ClassScanner(packageName, classLoader, null).scan(force);
    }

    public static Set<Class<?>> scanPackage(String packageName, boolean force) {
        return new ClassScanner(packageName).scan(force);
    }

    public static Set<Class<?>> scanPackage(String packageName, ClassLoader classLoader) {
        return new ClassScanner(packageName, classLoader, null).scan();
    }

    public static Set<Class<?>> scanPackage(String packageName, Predicate<Class<?>> classFilter) {
        return new ClassScanner(packageName, Thread.currentThread().getContextClassLoader(), classFilter).scan();
    }

    public static Set<Class<?>> scanPackage(String packageName, ClassLoader classLoader, Predicate<Class<?>> classFilter) {
        return new ClassScanner(packageName, classLoader, classFilter).scan();
    }

    public static Set<Class<?>> scanAllPackage() {
        return scanAllPackage("");
    }

    public static Set<Class<?>> scanAllPackage(String packageName) {
        return new ClassScanner(packageName).scan(true);
    }

    public static Set<Class<?>> scanAllPackage(String packageName, Predicate<Class<?>> classFilter) {
        return new ClassScanner(packageName, Thread.currentThread().getContextClassLoader(), classFilter).scan(true);
    }

    public static Set<Class<?>> scanAllPackage(String packageName, ClassLoader classLoader) {
        return new ClassScanner(packageName, classLoader).scan(true);
    }

    public static Set<Class<?>> scanAllPackage(String packageName, ClassLoader classLoader, Predicate<Class<?>> classFilter) {
        return new ClassScanner(packageName, classLoader, classFilter).scan(true);
    }

    /**
     * 获取注解的注解, 用于解决注解传递的问题
     *
     * @param subAnnotation     子注解
     * @param supAnnotationType 要获取的注解的字节码
     * @return subAnnotation 注解上字节码为 annotationType 的注解
     */
    private static Annotation getAnnotationFromAnnotation(Annotation subAnnotation, Class<? extends Annotation> supAnnotationType) {
        if (subAnnotation == null || supAnnotationType == null) {
            return null;
        }
        if (subAnnotation.annotationType() == supAnnotationType) {
            return subAnnotation;
        }
        for (Annotation annotation1 : subAnnotation.annotationType().getAnnotations()) {
            String name = annotation1.annotationType().getPackage().getName();
            if (name.startsWith("java") || name.startsWith("kot")) {
                continue;
            }
            return getAnnotationFromAnnotation(annotation1, supAnnotationType);
        }
        return null;
    }

    public static Set<Class<?>> scanWithAnnotation(String basePackage, Class<? extends Annotation> type) {
        return new ClassScanner(basePackage).scanWithAnnotation(type, true);
    }

    public static Set<Class<?>> scanWithSupperClass(String basePackage, Class<?> supperClass) {
        return new ClassScanner(basePackage).scanWithSupperClass(supperClass, true);
    }

    private static void aggregationAnnotation(Class<?> c, Set<Annotation> annotationList) {
        Annotation[] annotations = c.getAnnotations();
        Annotation[] declaredAnnotations = c.getDeclaredAnnotations();
        if (annotations.length == 0 || declaredAnnotations.length == 0 || "java.lang.annotation".equals(c.getPackage().getName())) {
            return;
        }
        for (Annotation annotation : annotations) {
            annotationList.add(annotation);
            aggregationAnnotation(annotation.annotationType(), annotationList);
        }
        for (Annotation annotation : declaredAnnotations) {
            annotationList.add(annotation);
            aggregationAnnotation(annotation.annotationType(), annotationList);
        }
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getPackageDirName() {
        return packageDirName;
    }

    public void setPackageDirName(String packageDirName) {
        this.packageDirName = packageDirName;
    }

    public Predicate<Class<?>> getClassPredicate() {
        return classPredicate;
    }

    public void setClassPredicate(Predicate<Class<?>> classPredicate) {
        this.classPredicate = classPredicate;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public Set<Class<?>> getClasses() {
        return classes;
    }

    public Set<String> getClassesOfLoadError() {
        return classesOfLoadError;
    }

    public boolean isInitialize() {
        return initialize;
    }

    public void setInitialize(boolean initialize) {
        this.initialize = initialize;
    }

    public boolean isIgnoreLoadError() {
        return ignoreLoadError;
    }

    public void setIgnoreLoadError(boolean ignoreLoadError) {
        this.ignoreLoadError = ignoreLoadError;
    }

    public Set<Class<?>> scan() {
        return scan(false);
    }

    public Set<Class<?>> scan(boolean forceScanJavaClassPaths) {
        clear();
        String basePackageFilePath = basePackage.replace('.', '/');
        try {
            Enumeration<URL> resources = classLoader.getResources(basePackageFilePath);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String protocol = resource.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(resource.getFile(), String.valueOf(StandardCharsets.UTF_8));
                    scanFile(new File(filePath), null);
                } else if ("jar".equals(protocol)) {
                    scanJar(((JarURLConnection) resource.openConnection()).getJarFile());
                }
            }
        } catch (IOException e) {
            classesOfLoadError.add(basePackageFilePath);
        }
        if (forceScanJavaClassPaths || classes.isEmpty()) {
            scanJavaClassPaths();
        }
        return classes;
    }

    public Set<Class<?>> scanWithAnnotation(Class<? extends Annotation> type) {
        if (classes.isEmpty()) {
            return scanWithAnnotation(type, true);
        }
        return scanWithAnnotation(type, false);
    }

    public Set<Class<?>> scanWithAnnotation(Class<? extends Annotation> type, boolean isRenew) {
        if (isRenew) {
            scan();
        }
        return classes.stream().filter(t -> {
            if (t.isAnnotation()) {
                return false;
            }
            if (t.getAnnotation(type) != null) {
                return true;
            }
            Set<Annotation> annotations = new HashSet<>();
            aggregationAnnotation(t, annotations);
            return annotations.stream().anyMatch(annotation -> annotation.annotationType() == type);
        }).collect(Collectors.toSet());
    }

    public Set<Class<?>> scanWithSupperClass(Class<?> supperClass) {
        if (classes.isEmpty()) {
            return scanWithSupperClass(supperClass, true);
        }
        return scanWithSupperClass(supperClass, false);
    }

    public Set<Class<?>> scanWithSupperClass(Class<?> supperClass, boolean isRenew) {
        if (isRenew) {
            scan();
        }
        return classes.stream().filter(t -> {
            Class<?>[] interfaces = t.getInterfaces();
            for (Class<?> inter : interfaces) {
                if (inter == supperClass) {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toSet());
    }

    protected Class<?> loadClass(String className) {
        ClassLoader loader = this.classLoader;
        if (null == loader) {
            loader = Thread.currentThread().getContextClassLoader();
            this.classLoader = loader;
        }
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className, this.initialize, loader);
        } catch (NoClassDefFoundError | ClassNotFoundException | UnsupportedClassVersionError e) {
            classesOfLoadError.add(className);
        } catch (Throwable e) {
            if (!this.ignoreLoadError) {
                throw new RuntimeException(e);
            } else {
                classesOfLoadError.add(className);
            }
        }
        return clazz;
    }

    private void scanJavaClassPaths() {
        final String[] javaClassPaths = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
        for (String classPath : javaClassPaths) {
            try {
                classPath = URLDecoder.decode(classPath, String.valueOf(StandardCharsets.UTF_8));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            scanFile(new File(classPath), null);
        }
    }

    /**
     * 扫描文件或目录中的类
     *
     * @param file    文件或目录
     * @param rootDir 包名对应classpath绝对路径
     */
    private void scanFile(File file, String rootDir) {
        if (file.isFile()) {
            final String fileName = file.getAbsolutePath();
            if (fileName.endsWith(".class")) {
                final String className = fileName//
                        .substring(rootDir.length(), fileName.length() - 6)//
                        .replace('/', '.');//
                addIfAccept(className);
            } else if (fileName.endsWith(".jar")) {
                try {
                    scanJar(new JarFile(file));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (file.isDirectory()) {
            final File[] files = file.listFiles();
            if (null != files) {
                for (File subFile : files) {
                    scanFile(subFile, (null == rootDir) ? subPathBeforePackage(file) : rootDir);
                }
            }
        }
    }

    private String subPathBeforePackage(File file) {
        String filePath = file.getAbsolutePath();
        filePath = StringUtils.subBefore(filePath, this.packageDirName, true);
        if (!StringUtils.hasText(filePath)) {
            return "";
        }
        return filePath.endsWith("/") ? filePath : filePath.concat("/");
    }

    private void scanJar(JarFile jar) {
        String name;
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            name = entry.getName().replace('/', '.');
            if (basePackage == null || basePackage.isEmpty() || name.startsWith(this.basePackage)) {
                if (name.endsWith(".class") && !entry.isDirectory()) {
                    final String className = name//
                            .substring(0, name.length() - 6)//
                            .replace('/', '.');
                    addIfAccept(loadClass(className));
                }
            }
        }
    }

    private void addIfAccept(Class<?> clazz) {
        if (null != clazz) {
            if (classPredicate == null || classPredicate.test(clazz)) {
                this.classes.add(clazz);
            }
        }
    }

    private void addIfAccept(String className) {
        if (className == null || className.isEmpty()) {
            return;
        }
        int classLen = className.length();
        int packageLen = this.basePackage.length();
        if (classLen == packageLen) {
            if (className.equals(this.basePackage)) {
                addIfAccept(loadClass(className));
            }
        } else if (classLen > packageLen) {
            addIfAccept(loadClass(className));
        }
    }

    private void clear() {
        this.classes = new HashSet<>();
        this.classesOfLoadError = new HashSet<>();
    }
}