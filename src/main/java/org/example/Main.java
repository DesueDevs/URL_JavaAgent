package org.example;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.logging.Logger;

import org.example.Intercepters.HttpConnectionAdvice;

public class Main {

    public static Map<String, String> httpFlags = Map.of("file:", "Ignore Urls", "jrt:", "Ignore Urls", "discord", "Exit", "discordapp", "Exit", "easyfor.me", "Exit");

    public static final Logger logger = Logger.getLogger("AntiRat");

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Java Agent started.");
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
                if (className != null && className.equals("java/net/URLConnection")) {
                    System.out.println("Transforming class: " + className);
                    return HttpConnectionAdvice.modifyURLConnectionClass(classfileBuffer);
                }
                return classfileBuffer;
            }
        });
    }
}