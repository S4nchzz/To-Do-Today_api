package com.to_do_api.todo_today_api.userAccount;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordComplexity {
    private String salt;
    private byte [] password;

    public PasswordComplexity(String password) {
        salt = generateSalt();
        this.password = hashPassword(salt + password);
    }

    public PasswordComplexity(String salt, String password) {
        this.salt = salt;
        this.password = hashPassword(salt + password);
    }

    private byte [] hashPassword (String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
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

    public byte [] getHashedPasswordByte() {
        return password;
    }
}
