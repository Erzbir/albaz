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
    implementation(project(":albaz-util"))
    implementation(project(":albaz-cglib"))
    api(project(":albaz-jcl"))
}

tasks.test {
    useJUnitPlatform()
}