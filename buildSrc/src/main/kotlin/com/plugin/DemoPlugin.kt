package com.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class DemoPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        println("Hello world!")
    }
}