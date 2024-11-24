dependencies {
    implementation(project(":albaz-common"))
    implementation(project(":albaz-jcl"))
}

tasks.withType<JavaExec> {
    jvmArgs("-verbose:class")
}

tasks.register<JavaExec>("Hot Load Debug") {
    jvmArgs("-verbose:class")
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("-verbose:class")
}