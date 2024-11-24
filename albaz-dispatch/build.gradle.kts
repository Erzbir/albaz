plugins {
    id("java")
}

dependencies {
    implementation(project(":albaz-common"))
    api(project(":albaz-dispatch-api"))
    implementation(project(":albaz-jcl"))
    implementation(project(":albaz-dispatch-core"))

    testImplementation(project(":albaz-jcl"))
}