package com.to_do_api.todo_today_api.repo.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    private int id;

    private String username;
    private String salt;
    private String pass;
    private String email;
    private boolean logged_in;

    public User() {
        // Constructor
    }

    public User(int id, String username, String salt, String pass, String email, boolean logged_in) {
        this.id = id;
        this.username = username;
        this.salt = salt;
        this.pass = pass;
        this.email = email;
        this.logged_in = logged_in;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLogged_in() {
        return logged_in;
    }

    public void setLogged_in(boolean logged_in) {
        this.logged_in = logged_in;
    }
}
