package com.to_do_api.todo_today_api.controller.teamManagement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.to_do_api.todo_today_api.repo.teams.Client_team_association;
import com.to_do_api.todo_today_api.repo.teams.RepositoryClientTeamAssociation;
import com.to_do_api.todo_today_api.repo.teams.RepositoryTeams;
import com.to_do_api.todo_today_api.repo.teams.Teams;
import com.to_do_api.todo_today_api.repo.user_temporal_tokens.RepositoryTemporalTokens;
import com.to_do_api.todo_today_api.repo.user_temporal_tokens.User_temporal_token;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/teams")
public class TeamManagementService {
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

        java.util.List<Teams> teamList = repositoryTeams.findAll();

        JSONObject jsonTeams = new JSONObject().put("responseStatus", true);
        for (Teams teams : teamList) {
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

        System.out.println(jsonTeams.toString());
        return ResponseEntity.ok(jsonTeams.toString());
    }
}
