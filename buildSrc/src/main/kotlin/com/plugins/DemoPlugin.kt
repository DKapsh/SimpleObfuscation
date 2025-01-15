package com.plugins

import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import com.plugins.tasks.CopyObfuscatorClass
import com.plugins.visitors.ObfuscatorClassVisitorFactory

class DemoPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.pluginManager.withPlugin("com.android.application") {
            val androidComponents =
                project.extensions.getByType(AndroidComponentsExtension::class.java)
            androidComponents.onVariants {
                    variant ->
                val capitalizedVariant = variant.name.replaceFirstChar { if (it
                        .isLowerCase()) it
                    .titlecase() else it.toString() }

                val generateCodeTask: TaskProvider<CopyObfuscatorClass> = project.tasks.register(
                    "generate${capitalizedVariant}Code",
                    CopyObfuscatorClass::class.java
                ) {
                    destinationDir.set(project.layout.buildDirectory.dir("generated/java"))
                }

                variant.sources.java?.addGeneratedSourceDirectory(generateCodeTask, CopyObfuscatorClass::destinationDir)
                variant.instrumentation.transformClassesWith(
                    ObfuscatorClassVisitorFactory::class.java,
                    InstrumentationScope.PROJECT
                ){}
            }
        }
    }
}