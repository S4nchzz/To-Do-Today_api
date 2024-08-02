package com.to_do_api.todo_today_api.repo.teams;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "client_team_association")
public class Client_team_association {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    
    @Column(name = "userid")
    private int userId;

    @Column(name = "teamkey")
    private String teamkey; 
    
    public Client_team_association() {
    }
    
    public Client_team_association(int userId, String teamkey) {
        this.userId = userId;
        this.teamkey = teamkey;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getTeamKey() {
        return teamkey;
    }
    
    public void setTeamKey(String teamkey) {
        this.teamkey = teamkey;
    }
}
