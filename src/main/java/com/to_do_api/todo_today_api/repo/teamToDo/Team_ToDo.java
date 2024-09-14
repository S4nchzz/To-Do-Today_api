package com.to_do_api.todo_today_api.repo.teamToDo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "team_todo")
public class Team_ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "teamkey")
    private String teamkey;

    @Column(name = "header")
    private String header;

    @Column(name = "content")
    private String content;

    @Column(name = "date")
    private String date;

    @Column(name="fav")
    private boolean fav;

    @Column(name="ended")
    private boolean ended;

    public Team_ToDo() {
    }

    public Team_ToDo(String teamkey, String header, String content, String date, boolean fav, boolean ended) {
        this.teamkey = teamkey;
        this.header = header;
        this.content = content;
        this.date = date;
        this.fav = fav;
        this.ended = ended;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeamkey() {
        return teamkey;
    }

    public void setTeamkey(String teamkey) {
        this.teamkey = teamkey;
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

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }
}
