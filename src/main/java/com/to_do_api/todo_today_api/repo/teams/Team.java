package com.to_do_api.todo_today_api.repo.teams;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @Column(name = "teamkey")
    private String teamkey;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "administrator")
    private int administrator;
    
    @Column(name = "publicgroup")
    private boolean publicGroup;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "date")
    private String date;

    public Team() {
    }

    // Default constructor initializing all parameters
    public Team(String teamkey, String name, String description, int administrator, boolean publicGroup, String password, String date) {
        this.teamkey = teamkey;
        this.name = name;
        this.description = description;
        this.administrator = administrator;
        this.publicGroup = publicGroup;
        this.password = password;
        this.date = date;
    }

    // Constructor for a new creation of a team
    public Team(String name, String description, int administrator, boolean publicGroup, String password, String date) {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.subSequence(0, 4).toString();

        this.teamkey = uuid;
        this.name = name;
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
        return name;
    }

    public void setTitle(String name) {
        this.name = name;
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
