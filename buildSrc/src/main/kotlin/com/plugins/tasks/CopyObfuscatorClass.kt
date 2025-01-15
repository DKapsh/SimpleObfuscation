package com.plugins.tasks

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths

abstract class CopyObfuscatorClass: org.gradle.api.DefaultTask() {
    @get:OutputDirectory
    abstract val destinationDir: DirectoryProperty

    @TaskAction
    fun copy() {
        val clsName = "/Obfuscator.java"
        val pkgName = "/com/simple/obfuscator"
        val dst = File(destinationDir.asFile.get(), "${pkgName}${clsName}")
        dst.parentFile.mkdirs()

        if(!dst.exists()) {

            val src: InputStream? = javaClass.getResourceAsStream(clsName)
            if (src != null) {
                Files.copy(src, Paths.get(dst.toURI()))
            }
        }

    }
}