package com.to_do_api.todo_today_api.repo.user;

import jakarta.persistence.Column;
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
    @Column(name = "id")
    private int id;
    
    @Column(name = "username")
    private String username;
    
    @Column(name = "salt")
    private String salt;
    
    @Column(name = "pass")
    private byte [] pass;
    
    @Column(name = "email")
    private String email;

    @Column(name = "email_verified")
    private boolean email_verified;
    
    @Column(name = "logged_in")
    private boolean logged_in;
    
    @Column(name = "ingroup")
    private boolean ingroup;

    public User() {

    }
    
    public User(String username, String salt, byte [] pass, String email, boolean email_verified, boolean logged_in) {
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

    public boolean isEmailVerified() {
        return this.email_verified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.email_verified = emailVerified;
    }

    public boolean isLogged_in() {
        return logged_in;
    }

    public void setLogged_in(boolean logged_in) {
        this.logged_in = logged_in;
    }

    public boolean isInGroup() {
        return this.ingroup;
    }

    public void setInGroup(boolean ingroup) {
        this.ingroup = ingroup;
    }
}