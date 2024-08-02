package com.to_do_api.todo_today_api.repo.teams;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "teams")
public class Teams {
    @Id
    @Column(name = "teamkey")
    private String teamkey;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "administrator")
    private int administrator;
    
    @Column(name = "PUBLICGROUP")
    private boolean publicGroup;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "date")
    private String date;

    public Teams() {
    }

    public Teams(String teamkey, String title, String description, int administrator, boolean publicGroup, String password, String date) {
        this.teamkey = teamkey;
        this.title = title;
        this.description = description;
        this.administrator = administrator;
        this.publicGroup = publicGroup;
        this.password = password;
        this.date = date;
    }

    public String getTeamkey() {
        return teamkey;
    }

    public void setTeam_key(String teamkey) {
        this.teamkey = teamkey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAdministrator() {
        return administrator;
    }

    public void setAdministrator(int administrator) {
        this.administrator = administrator;
    }

    public boolean isPublicGroup() {
        return publicGroup;
    }

    public void setPublicGroup(boolean publicGroup) {
        this.publicGroup = publicGroup;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
