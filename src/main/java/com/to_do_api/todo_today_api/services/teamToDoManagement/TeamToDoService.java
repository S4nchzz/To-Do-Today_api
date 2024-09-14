package com.to_do_api.todo_today_api.services.teamToDoManagement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.to_do_api.todo_today_api.repo.teamToDo.RepositoryTeam_ToDo;
import com.to_do_api.todo_today_api.repo.teamToDo.Team_ToDo;
import com.to_do_api.todo_today_api.repo.teams.RepositoryClientTeamAssociation;
import com.to_do_api.todo_today_api.repo.teams.RepositoryTeams;
import com.to_do_api.todo_today_api.repo.teams.Team;
import com.to_do_api.todo_today_api.repo.user.User;
import com.to_do_api.todo_today_api.services.CheckUserExist;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/teamToDo")
public class TeamToDoService {
    @Autowired
    private RepositoryTeam_ToDo repositoryTeam_ToDo;

    @Autowired
    private RepositoryTeams repositoryTeams;
    @Autowired RepositoryClientTeamAssociation repositoryClientTeamAssociation;

    @Autowired
    private CheckUserExist checkUserExist;

    @PostMapping("/addTeamToDo")
    public ResponseEntity<String> addTeamToDo(@RequestHeader("Authorization") String token, @RequestBody String body) {
        User user = checkUserExist.check(token);
        Team team = repositoryTeams.findByTeamkey(repositoryClientTeamAssociation.findByUserId(user.getId()).getTeamKey());

        if (user != null && team != null) {
            // User and his team exist
            JSONObject todo = new JSONObject(body);
            repositoryTeam_ToDo.save(new Team_ToDo(team.getTeamkey(), todo.getString("header"), todo.getString("content"), todo.getString("date"), todo.getBoolean("fav"), todo.getBoolean("ended")));
            return ResponseEntity.ok(new JSONObject().put("teamToDoCreated", true).toString());
        }

        return ResponseEntity.ok(new JSONObject().put("teamToDoCreated", false).toString());
    }

    @PostMapping("/getTeamToDos")
    public ResponseEntity<String> getTeamToDos(@RequestHeader("Authorization") String token) {
        User user = checkUserExist.check(token);

        Team team = repositoryTeams.findByTeamkey(repositoryClientTeamAssociation.findByUserId(user.getId()).getTeamKey());

        if (user != null && team != null) {
            List<Team_ToDo> teamToDoList = repositoryTeam_ToDo.findByTeamkey(team.getTeamkey());

            JSONObject allEntries = new JSONObject();
            int toDoKeyIterator = 0;
            for (Team_ToDo todo : teamToDoList) {
                JSONObject currentToDo = new JSONObject()
                .put("id", todo.getId())
                .put("teamkey", todo.getTeamkey())
                .put("header", todo.getHeader())
                .put("content", todo.getContent())
                .put("date", todo.getDate())
                .put("fav", todo.isFav())
                .put("ended", todo.isEnded());

                allEntries.put(String.valueOf(toDoKeyIterator), currentToDo.toString());
            }

            return ResponseEntity.ok(allEntries.toString());
        }

        return ResponseEntity.ok(new JSONObject().put("generated", false).toString());
    }

    @PostMapping("/completeTeamToDo")
    public ResponseEntity<String> completeTeamToDo(@RequestHeader("Authorization") String token, @RequestBody String entity) {
        User user = checkUserExist.check(token);

        Team userIsInTeam = repositoryTeams.findByTeamkey(repositoryClientTeamAssociation.findByUserId(user.getId()).getTeamKey());

        if (user != null && userIsInTeam != null) {
            Team_ToDo todo = repositoryTeam_ToDo.findById(new JSONObject(entity).getInt("id"));
            todo.setEnded(new JSONObject(entity).getBoolean("completedStatus"));
            
            repositoryTeam_ToDo.save(todo);

            return ResponseEntity.ok(new JSONObject().put("teamToDoCompleted", true).toString());
        }
        
        return ResponseEntity.ok(new JSONObject().put("teamToDoCompleted", false).toString());
    }

    @PostMapping("/deleteTeamToDo")
    public ResponseEntity<String> deleteTeamToDo(@RequestHeader("Authorization") String token, @RequestBody String entity) {
        User user = checkUserExist.check(token);

        Team userIsInTeam = repositoryTeams
                .findByTeamkey(repositoryClientTeamAssociation.findByUserId(user.getId()).getTeamKey());

        if (user != null && userIsInTeam != null) {
            repositoryTeam_ToDo.deleteById(new JSONObject(entity).getInt("id"));

            return ResponseEntity.ok(new JSONObject().put("teamToDoCompleted", true).toString());
        }

        return ResponseEntity.ok(new JSONObject().put("teamToDoCompleted", false).toString());
    }
}
