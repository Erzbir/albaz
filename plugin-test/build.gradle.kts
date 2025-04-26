plugins {
    id("java")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    compileOnly(project(":albaz-plugin:albaz-plugin-api"))
    implementation("com.google.code.gson:gson:2.11.0")
}

tasks.shadowJar {
    archiveClassifier.set("")
    mergeServiceFiles()
}