package com.to_do_api.todo_today_api.repo.verify_email_codes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "verify_email_codes")
public class Verify_email_codes {
    @Id
    private int id;

    @Column(name = "code")
    private int code;
    
    @Column(name = "email")
    private String email;

    public Verify_email_codes(int code, String email) {
        this.code = code;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Verify_email_codes() {
    }
}
