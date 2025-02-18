package com.plugins.visitors

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import kotlin.math.absoluteValue
import kotlin.random.Random

internal class ObfuscatorMethodVisitor(
    api: Int,
    next: MethodVisitor,
) : MethodVisitor(api, next) {
    private var additionalStack = 0;
    override fun visitLdcInsn(
        value: Any
    ) {
        if (value is String) {
            val random = Random(System.nanoTime())
            additionalStack += 7

            mv.visitLdcInsn(value.length)
            mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_CHAR)


            for ((index, c) in value.toCharArray().withIndex()) {
                val addKey = random.nextLong().absoluteValue
                var mulKey = random.nextLong().absoluteValue

                if (mulKey % 2 == 0L) {
                    mulKey++
                }
                val obfuscatedChar = Obfuscator.obfuscateChar(c, addKey, mulKey)
                mv.visitInsn(Opcodes.DUP)
                mv.visitLdcInsn(index)
                mv.visitLdcInsn(obfuscatedChar)

                mv.visitLdcInsn(addKey);
                mv.visitLdcInsn(mulKey);
                mv.visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    "com/simple/obfuscator/Obfuscator",
                    "deobfuscateChar",
                    "(CJJ)C",
                    false
                )

                mv.visitInsn(Opcodes.CASTORE)
            }

            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "java/lang/String",
                "valueOf",
                "([C)Ljava/lang/String;",
                false
            )
        } else {
            super.visitLdcInsn(value)
        }
    }

    override fun visitMaxs(maxStack: Int, maxLocals: Int) {
        super.visitMaxs(maxStack + additionalStack, maxLocals)
        additionalStack = 0
    }

    private class Obfuscator {
        companion object {
            fun obfuscateChar(c: Char, addKey: Long, mulKey: Long): Any? {
                val obfuscatedCode = ((c.code.toLong() + addKey) % 256 * mulKey % 256) % 256
                return Char(obfuscatedCode.toUShort())
            }
        }
    }
}