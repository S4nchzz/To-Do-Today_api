package com.to_do_api.todo_today_api.repo.user_temporal_tokens;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User_temporal_token {
    @Id
    private String token;
    private int userID;

    public User_temporal_token() {

    }

    public User_temporal_token(String token, int userid) {
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