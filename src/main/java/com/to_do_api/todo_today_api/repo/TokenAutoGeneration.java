package com.to_do_api.todo_today_api.repo;

import java.util.Random;

public class TokenAutoGeneration {
    private static TokenAutoGeneration instance = new TokenAutoGeneration();
    
    private TokenAutoGeneration () {

    }

    public static TokenAutoGeneration getInstance() {
        return instance;
    }

    public String generateToken() {
        Random rm = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 255; i++) {
            int i_char = 33 + rm.nextInt(94);
            sb.append((char)i_char);
        }

        return sb.toString();
    }
}
