buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

plugins {
    id("java-library")
    id("java")
    id("application")
    id("distribution")
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.1"
}

group = "com.erzbir.albaz"
version = "1.0.0"

dependencies {

}

val javaVersion = JavaVersion.VERSION_21
val gradleVersion = "8.13"
val encoding = "UTF-8"


subprojects {

    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.johnrengelman.shadow")

    dependencies {
        testImplementation(platform("org.junit:junit-bom:5.10.3"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testRuntimeOnly("org.apache.logging.log4j:log4j-core:2.24.3")

    }
}

allprojects {

    version = rootProject.version
    group = rootProject.group

    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google()
    }

    ext {
        set("javaVersion", javaVersion)
        set("gradleVersion", gradleVersion)
        set("encoding", encoding)
    }

    tasks.withType<JavaCompile> {
        options.encoding = encoding

        java {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }
    }

    tasks.withType<JavaExec> {
        workingDir = rootDir
    }

    tasks.withType<Jar> {
        enabled
    }


    tasks.withType<Wrapper> {
        this.gradleVersion = gradleVersion
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        debugOptions.enabled
    }

    tasks.test {
        useJUnitPlatform()
    }
}

