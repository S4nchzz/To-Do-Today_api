package com.to_do_api.todo_today_api.repo.todo;

import org.json.JSONObject;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "todo")
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userid;
    private String header;
    private String content;

    private String date;
    private Boolean fav;
    private Boolean ended;

    public ToDo() {
    }

    public ToDo(int userid, String header, String content, String date, Boolean fav, Boolean ended) {
        this.userid = userid;
        this.header = header;
        this.content = content;
        this.date = date;
        this.fav = fav;
        this.ended = ended;
    }

    public ToDo(int id, int userid, String header, String content, String date, Boolean fav, Boolean ended) {
        this.id = id;
        this.userid = userid;
        this.header = header;
        this.content = content;
        this.date = date;
        this.fav = fav;
        this.ended = ended;
        }

    public JSONObject getJson() {
        JSONObject json = new JSONObject()
        .put("id", this.id)
        .put("userid", this.userid)
        .put("header", this.header)
        .put("content", this.content)
        .put("date", this.date)
        .put("fav", this.fav)
        .put("ended", this.ended);
        
        return json;
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

    public Boolean isEnded() {
        return ended;
    }

    public void setEnded(Boolean ended) {
        this.ended = ended;
    }
}
