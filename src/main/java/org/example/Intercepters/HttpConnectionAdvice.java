package org.example.Intercepters;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.jar.asm.*;
import org.example.Main;

import java.util.Map;

public class HttpConnectionAdvice {
    public static byte[] modifyURLConnectionClass(byte[] classfileBuffer) {
        try {
            ClassReader classReader = new ClassReader(classfileBuffer);
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

            classReader.accept(new ClassVisitor(Opcodes.ASM9, classWriter) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                    if ("<init>".equals(name) && "(Ljava/net/URL;)V".equals(descriptor)) {
                        return new MethodVisitor(Opcodes.ASM9, mv) {
                            @Override
                            public void visitCode() {
                                super.visitCode();
                                Label skipPrintLabel = new Label();
                                for (Map.Entry<String, String> set : Main.httpFlags.entrySet()) {
                                    mv.visitVarInsn(Opcodes.ALOAD, 1);
                                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/net/URL", "toString", "()Ljava/lang/String;", false);
                                    mv.visitLdcInsn(set.getKey());
                                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "contains", "(Ljava/lang/CharSequence;)Z", false);
                                    Label label = new Label();
                                    if (set.getValue().equals("Ignore Urls")) {
                                        mv.visitJumpInsn(Opcodes.IFNE, skipPrintLabel);
                                    } else if (set.getValue().equals("Exit")) {
                                        Main.logger.info("Found discord url, exiting...");
                                        mv.visitJumpInsn(Opcodes.IFEQ, label);
                                        mv.visitLdcInsn(66);
                                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "exit", "(I)V", false);
                                    }
                                    mv.visitLabel(label);
                                }

                                mv.visitVarInsn(Opcodes.ALOAD, 1);
                                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/net/URL", "toString", "()Ljava/lang/String;", false);
                                mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                                mv.visitInsn(Opcodes.SWAP);
                                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                                mv.visitLabel(skipPrintLabel);
                            }
                        };
                    }
                    return mv;
                }
            }, 0);

            return classWriter.toByteArray();
        } catch (Exception e) {
            System.err.println("Exception during class modification: " + e.getMessage());
            e.printStackTrace();
            return classfileBuffer;
        }
    }
}