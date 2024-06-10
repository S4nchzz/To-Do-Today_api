package com.to_do_api.todo_today_api.userAccount;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordComplexity {
    private final String salt;
    private byte [] password;

    public PasswordComplexity(String password) {
        salt = generateSalt();
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
            this.password = mDigest.digest((salt + password).getBytes());
        } catch (NoSuchAlgorithmException e) {
            // ? LOG: Failed to hash password
            e.printStackTrace();
        }
    }

    private String generateSalt() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 15; i++) {
            sb.append((char) (33 + (int) (Math.random() * 94)));
        }

        return sb.toString();
    }

    public String getSalt() {
        return this.salt;
    }

    public String getHashedPassword() {
        String str = new String(password, StandardCharsets.UTF_8);
        return str;
    }
}
