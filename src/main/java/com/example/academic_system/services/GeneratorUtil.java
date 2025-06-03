package com.example.academic_system.services;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

public class GeneratorUtil {

    private static final SecureRandom random = new SecureRandom();
    private static final Set<String> usedNims = new HashSet<>();
    private static final Set<String> usedNips = new HashSet<>();


    public static String generateNim() {
        String nim;
        do {
            nim = "M" + generateRandomNumberString(9);
        } while (usedNims.contains(nim));
        usedNims.add(nim);
        return nim;
    }


    public static String generateNip() {
        String nip;
        do {
            nip = "D" + generateRandomNumberString(9);
        } while (usedNips.contains(nip));
        usedNips.add(nip);
        return nip;
    }


    private static String generateRandomNumberString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // 0 - 9
        }
        return sb.toString();
    }
}
