package com.simple.obfuscator;

public class Obfuscator {

    public static char deobfuscateChar(char input, long addKey, long mulKey) {
        long modKey = 256;
        long modInverse = modularInverse(mulKey, modKey);
        long originalCode = (((input * modInverse) % modKey - addKey) % modKey + modKey) % modKey;
        return (char) originalCode;
    }


    private static long modularInverse(long a, long m) {
        long m0 = m, x0 = 0, x1 = 1;
        if (m == 1) return 0;

        while (a > 1) {
            long q = a / m0;
            long t = m0;

            m0 = a % m0;
            a = t;
            t = x0;

            x0 = x1 - q * x0;
            x1 = t;
        }

        if (x1 < 0) x1 += m;

        return x1;
    }
}
