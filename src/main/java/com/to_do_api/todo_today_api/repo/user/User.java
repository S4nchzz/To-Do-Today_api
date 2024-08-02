package com.to_do_api.todo_today_api.repo.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String salt;
    private byte [] pass;
    private String email;
    private boolean logged_in;

    public User() {

    }
    
    public User(String username, String salt, byte [] pass, String email, boolean logged_in) {
        this.username = username;
        this.salt = salt;
        this.pass = pass;
        this.email = email;
        this.logged_in = logged_in;
    }

    public int getId() {
        return id;
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

    public byte [] getPass() {
        return pass;
    }

    public void setPass(byte [] pass) {
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