plugins {
    id("java")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(project(":albaz-plugin:albaz-plugin-api"))
    implementation(project(":albaz-plugin:albaz-plugin-core"))
    implementation("com.google.code.gson:gson:2.11.0")
}

tasks.withType<JavaExec> {

}