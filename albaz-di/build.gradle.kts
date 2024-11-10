plugins {
    id("java")
}

group = "com.erzbir"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:24.1.0")
    implementation(project(":albaz-util"))
    implementation(project(":albaz-cglib"))
    implementation(project(":albaz-jcl"))
    implementation("org.apache.logging.log4j:log4j-core:2.24.1")
    implementation("org.slf4j:slf4j-api")
}

tasks.test {
    useJUnitPlatform()
}