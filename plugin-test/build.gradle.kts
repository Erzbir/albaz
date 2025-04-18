plugins {
    id("java")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(project(":albaz-plugin-api"))
    implementation("com.google.code.gson:gson:2.11.0")
}

tasks.withType<JavaExec> {

}