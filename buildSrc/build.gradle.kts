plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("demo-plugin") {
            id = "demo-plugin"
            implementationClass = "com.plugins.DemoPlugin"
        }
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:8.3.1")
    implementation(kotlin("gradle-plugin", "1.8.10"))
    implementation(kotlin("android-extensions"))
    implementation(kotlin("script-runtime"))
}