package com.example.academic_system.services;

import java.util.Random;

public class GeneratorUtil {

    public static String generateNim() {
        return "22" + new Random().nextInt(999999);
    }

    public static String generateNip() {
        return "D" + System.currentTimeMillis();
    }


}

