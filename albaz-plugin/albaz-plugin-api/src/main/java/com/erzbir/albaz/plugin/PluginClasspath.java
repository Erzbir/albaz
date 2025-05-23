/*
 * Copyright (C) 2012-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.erzbir.albaz.plugin;

import java.util.*;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class PluginClasspath {

    private final Set<String> classesDirectories = new HashSet<>();
    private final Set<String> jarsDirectories = new HashSet<>();

    public Set<String> getClassesDirectories() {
        return classesDirectories;
    }

    public PluginClasspath addClassesDirectories(String... classesDirectories) {
        return addClassesDirectories(Arrays.asList(classesDirectories));
    }

    public PluginClasspath addClassesDirectories(Collection<String> classesDirectories) {
        this.classesDirectories.addAll(classesDirectories);

        return this;
    }

    public Set<String> getJarsDirectories() {
        return jarsDirectories;
    }

    public PluginClasspath addJarsDirectories(String... jarsDirectories) {
        return addJarsDirectories(Arrays.asList(jarsDirectories));
    }

    public PluginClasspath addJarsDirectories(Collection<String> jarsDirectories) {
        this.jarsDirectories.addAll(jarsDirectories);

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PluginClasspath that)) return false;
        return classesDirectories.equals(that.classesDirectories) &&
                jarsDirectories.equals(that.jarsDirectories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classesDirectories, jarsDirectories);
    }

}
