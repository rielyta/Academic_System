package com.example.academic_system.services;

import java.util.Random;

public class GeneratorUtil {

    public static String generateNim() {
        return "22" + new Random().nextInt(999999); // contoh: 22xxxxxx
    }

    public static String generateNip() {
        return "D" + System.currentTimeMillis(); // atau pola lainnya
    }


}

