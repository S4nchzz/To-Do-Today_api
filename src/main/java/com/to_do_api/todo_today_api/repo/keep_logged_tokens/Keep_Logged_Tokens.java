package com.to_do_api.todo_today_api.repo.keep_logged_tokens;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "keep_logged_tokens")
public class Keep_Logged_Tokens {
    @Id
    private String token;
    private int userid;

    public Keep_Logged_Tokens () {
    }

    public Keep_Logged_Tokens (String token, int userid) {
        this.token = token;
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
