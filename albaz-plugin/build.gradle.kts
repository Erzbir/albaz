plugins {
    id("java")
}

dependencies {
    implementation(project(":albaz-common"))
}

tasks.withType<JavaExec> {
    jvmArgs("-verbose:class")
}

tasks.register<JavaExec>("Hot Load Debug") {
    jvmArgs("-verbose:class")
}

tasks.test {
    useJUnitPlatform()
//    jvmArgs("-verbose:class")
}