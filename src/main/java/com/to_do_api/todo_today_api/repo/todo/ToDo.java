package com.to_do_api.todo_today_api.repo.todo;

import org.json.JSONObject;

import jakarta.persistence.Column;
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
    @Column(name = "id")
    private int id;

    @Column(name = "userid")
    private int userid;

    @Column(name = "header")
    private String header;

    @Column(name = "content")
    private String content;

    @Column(name = "date")
    private String date;

    @Column(name = "fav")
    private Boolean fav;

    @Column(name = "ended")
    private Boolean ended;

    @Column(name = "team")
    private String team;

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

    public ToDo(int id, int userid, String header, String content, String date, Boolean fav, Boolean ended, String team) {
        this.id = id;
        this.userid = userid;
        this.header = header;
        this.content = content;
        this.date = date;
        this.fav = fav;
        this.ended = ended;
        this.team = team;

        }

    public JSONObject getJson() {
        JSONObject json = new JSONObject()
        .put("id", this.id)
        .put("userid", this.userid)
        .put("header", this.header)
        .put("content", this.content)
        .put("date", this.date)
        .put("fav", this.fav)
        .put("ended", this.ended)
        .put("team", this.team);
        
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

    public String getGroup() {
        return this.team;
    }

    public void setGroup(String team) {
        this.team = team;
    }
}
