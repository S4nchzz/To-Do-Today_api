package com.to_do_api.todo_today_api.repo.to_dos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class To_Dos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userid;
    private String header;
    private String content;

    @Column(insertable = false)
    private String date;
    private Boolean fav;

    public To_Dos() {
    }

    public To_Dos(int userid, String header, String content, Boolean fav) {
        this.userid = userid;
        this.header = header;
        this.content = content;
        this.fav = fav;
    }

    public To_Dos(int id, int userid, String header, String content, String date, Boolean fav) {
        this.id = id;
        this.userid = userid;
        this.header = header;
        this.content = content;
        this.date = date;
        this.fav = fav;
    }

    public int getId() {
        return id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean isFav() {
        return fav;
    }

    public void setFav(Boolean fav) {
        this.fav = fav;
    }
}
