package com.plugins.visitors

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

abstract class ObfuscatorClassVisitorFactory :
    AsmClassVisitorFactory<ObfuscatorClassVisitorFactory.ObfuscatorParameters> {

    interface ObfuscatorParameters : InstrumentationParameters {
    }

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor = ObfuscatorClassVisitor(
        api = instrumentationContext.apiVersion.get(),
        next = nextClassVisitor,
    )

    override fun isInstrumentable(classData: ClassData): Boolean {
        return classData.className != "com/simple/obfuscator/Obfuscator"
    }

    private class ObfuscatorClassVisitor(
        api: Int,
        next: ClassVisitor,
    ) : ClassVisitor(api, next) {

        override fun visitMethod(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String>?
        ): MethodVisitor {
            val next = super.visitMethod(access, name, descriptor, signature, exceptions)
            return ObfuscatorMethodVisitor(api, next)
        }
    }
}