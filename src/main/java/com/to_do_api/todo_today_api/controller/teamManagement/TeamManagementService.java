package com.to_do_api.todo_today_api.controller.teamManagement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.to_do_api.todo_today_api.repo.teams.Client_team_association;
import com.to_do_api.todo_today_api.repo.teams.RepositoryClientTeamAssociation;
import com.to_do_api.todo_today_api.repo.teams.RepositoryTeams;
import com.to_do_api.todo_today_api.repo.teams.Team;
import com.to_do_api.todo_today_api.repo.user.RepositoryUser;
import com.to_do_api.todo_today_api.repo.user.User;
import com.to_do_api.todo_today_api.repo.user_temporal_tokens.RepositoryTemporalTokens;
import com.to_do_api.todo_today_api.repo.user_temporal_tokens.User_temporal_token;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/teams")
public class TeamManagementService {
    @Autowired
    private RepositoryUser repositoryUser;

    @Autowired
    private RepositoryTeams repositoryTeams;
    
    @Autowired
    private RepositoryTemporalTokens repositoryTemporalTokens;

    @Autowired
    private RepositoryClientTeamAssociation repositoryClientTeamAssociation;

    @PostMapping("/getGroups")
    public ResponseEntity<String> getGroups(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");

        User_temporal_token user_temporal_token = repositoryTemporalTokens.findByToken(token);

        if (user_temporal_token == null) {
            return ResponseEntity.ok(new JSONObject().put("responseStatus", false).toString());
        }

        java.util.List<Team> teamList = repositoryTeams.findAll();

        JSONObject jsonTeams = new JSONObject().put("responseStatus", true);
        for (Team teams : teamList) {
            JSONObject team = new JSONObject()
            .put("teamkey", teams.getTeamkey())
            .put("title", teams.getTitle())
            .put("description", teams.getDescription())
            .put("administrator", teams.getAdministrator())
            .put("publicgroup", teams.isPublicGroup())
            .put("password", teams.getPassword())
            .put("date", teams.getDate());

            jsonTeams.put(teams.getTeamkey(), team);
        }
        return ResponseEntity.ok(jsonTeams.toString());
    }

    @PostMapping("/associateUser")
    public ResponseEntity<String> associateUser(@RequestHeader("Authorization") String tkn, @RequestBody String body) {
        String token = tkn.replace("Bearer ", "");
        
        User_temporal_token user_temporal_token = repositoryTemporalTokens.findByToken(token);
        if (user_temporal_token != null) {
            User user = repositoryUser.findById(user_temporal_token.getUserId());   

            Client_team_association client_team_association = repositoryClientTeamAssociation.findByUserId(user.getId());
            if (client_team_association != null) {
                return ResponseEntity.ok(new JSONObject().put("joined", false).toString());
            }
            
            JSONObject teamJson = new JSONObject(body);

            repositoryClientTeamAssociation.save(new Client_team_association(user.getId(), teamJson.getString("groupId")));
            
            // ? Set user value isInGroup to 1
            user.setInGroup(true);
            repositoryUser.save(user);

            return ResponseEntity.ok(new JSONObject().put("joined", true).toString());
        }
        
        return ResponseEntity.ok(new JSONObject().put("joined", false).toString());
    }

    @PostMapping("/getGroupData")
    public ResponseEntity<String> getGroupData(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        User_temporal_token user_temporal_token = repositoryTemporalTokens.findByToken(token);
        Client_team_association client_team_association = repositoryClientTeamAssociation.findByUserId(user_temporal_token.getUserId());

        if (client_team_association == null) {
            return ResponseEntity.ok(new JSONObject().put("dataExist", false).toString());
        }

        Team team = repositoryTeams.findByTeamkey(client_team_association.getTeamKey());

        JSONObject groupData = new JSONObject()
        .put("dataExist", true)
        .put("teamKey", team.getTeamkey())
        .put("title", team.getTitle())
        .put("description", team.getDescription())
        .put("administrator", team.getAdministrator())
        .put("publicGroup", team.isPublicGroup())
        .put("password", team.getPassword())
        .put("date", team.getDate());

        return ResponseEntity.ok(groupData.toString());
    }
    
    @PostMapping("/createNewTeam")
    public ResponseEntity<String> createNewTeam(@RequestHeader("Authorization") String token, @RequestBody String body) {
        token = token.replace("Bearer ", "");
        User_temporal_token user_temporal_token = repositoryTemporalTokens.findByToken(token);

        if (user_temporal_token == null) {
            return ResponseEntity.ok(new JSONObject().put("newGroupRequest", false).toString());
        }

        JSONObject newTeamJsonData = new JSONObject(body.toString());
        String teamEmptyPassword = newTeamJsonData.getString("password");

        if (teamEmptyPassword == null) {
            teamEmptyPassword = "";
        }

        Team newTeam = new Team(newTeamJsonData.getString("name"), newTeamJsonData.getString("description"), user_temporal_token.getUserId(), 
                newTeamJsonData.getBoolean("private"), teamEmptyPassword);

        Team hasBeenSaved = repositoryTeams.save(newTeam);
        
        if (hasBeenSaved != null) {
            return ResponseEntity.ok(new JSONObject().put("newGroupRequest", true).toString());
        }

        return ResponseEntity.ok(new JSONObject().put("newGroupRequest", false).toString());

    }
}
