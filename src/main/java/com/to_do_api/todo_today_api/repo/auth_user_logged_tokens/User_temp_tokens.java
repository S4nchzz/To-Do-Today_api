package com.to_do_api.todo_today_api.repo.auth_user_logged_tokens;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User_temp_tokens {
    @Id
    private String token;
    private int user_ID;

    public User_temp_tokens() {
        
    }

    public User_temp_tokens (String token, int user_id) {
        this.token = token;
        this.user_ID = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    public int getUser_ID() {
        return user_ID;
    }
    
    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }
}
