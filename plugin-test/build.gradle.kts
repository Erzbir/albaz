plugins {
    id("java")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(project(":albaz-plugin"))
    implementation("com.google.code.gson:gson:2.11.0")
}

tasks.withType<JavaExec> {
    jvmArgs("--illegal-access=deny")
}