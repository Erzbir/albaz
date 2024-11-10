plugins {
    id("java")
}

dependencies {
    implementation(project(":albaz-common"))
    api(project(":albaz-dispatch-api"))
    implementation(project(":albaz-dispatch-core"))
}