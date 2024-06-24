package com.to_do_api.todo_today_api.repo.user_temp_tokens;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User_temp_tokens {
    @Id
    private String token;
    private int userID;

    public User_temp_tokens() {

    }

    public User_temp_tokens(String token, int userid) {
        this.token = token;
        this.userID = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserID() {
        return userID;
    }
}