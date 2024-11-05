plugins {
    id("java")
}

group = "com.erzbir"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    api("org.apache.logging.log4j:log4j-core:2.24.1")
    api("org.slf4j:slf4j-api")
}

tasks.test {
    useJUnitPlatform()
}