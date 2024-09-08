package com.to_do_api.todo_today_api.services.teamManagement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.to_do_api.todo_today_api.repo.teams.Client_team_association;
import com.to_do_api.todo_today_api.repo.teams.RepositoryClientTeamAssociation;
import com.to_do_api.todo_today_api.repo.teams.RepositoryTeams;
import com.to_do_api.todo_today_api.repo.teams.Team;
import com.to_do_api.todo_today_api.repo.user.RepositoryUser;
import com.to_do_api.todo_today_api.repo.user.User;
import com.to_do_api.todo_today_api.repo.user_temporal_tokens.RepositoryTemporalTokens;
import com.to_do_api.todo_today_api.services.CheckUserExist;

import jakarta.persistence.NonUniqueResultException;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/teams")
public class TeamManagementService {
    private CheckUserExist checkUser;
    private RepositoryUser repositoryUser;
    private RepositoryTeams repositoryTeams;
    private RepositoryClientTeamAssociation repositoryClientTeamAssociation;

    public TeamManagementService(RepositoryUser repositoryUser, RepositoryTeams repositoryTeams, RepositoryTemporalTokens repositoryTemporalTokens, RepositoryClientTeamAssociation repositoryClientTeamAssociation, CheckUserExist checkUserExist) {
        this.repositoryUser = repositoryUser;
        this.repositoryTeams = repositoryTeams;
        this.repositoryClientTeamAssociation = repositoryClientTeamAssociation;

        this.checkUser = checkUserExist;
    }
    
    @PostMapping("/getGroups")
    public ResponseEntity<String> getGroups(@RequestHeader("Authorization") String token) {
        User user = checkUser.check(token);

        if (user == null) {
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
            .put("date", teams.getDate())
            .put("nMembers", repositoryClientTeamAssociation.getMembersByTeamKey(teams.getTeamkey()));

            jsonTeams.put(teams.getTeamkey(), team);
        }
        return ResponseEntity.ok(jsonTeams.toString());
    }

    @PostMapping("/associateUser")
    public ResponseEntity<String> associateUser(@RequestHeader("Authorization") String tkn, @RequestBody String body) {
        User user = checkUser.check(tkn);

        if (user != null) {
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

    @PostMapping("/associateUserToPrivateTeam")
    public ResponseEntity<String> associateUserToPrivateTeam(@RequestHeader("Authorization") String tkn, @RequestBody String body) {
        User user = checkUser.check(tkn);

        JSONObject group = new JSONObject(body);
        Team team = repositoryTeams.findByName(group.getString("name"));

        if (user != null && team != null && team.getPassword().equals(group.getString("password"))) {
            repositoryClientTeamAssociation.save(new Client_team_association(user.getId(), team.getTeamkey()));
            user.setInGroup(true);
            repositoryUser.save(user);

            return ResponseEntity.ok(new JSONObject().put("userJoinedToPrivateTeam", true).toString());
        }

        return ResponseEntity.ok(new JSONObject().put("userJoinedToPrivateTeam", false).toString());
    }
    

    @PostMapping("/getGroupData")
    public ResponseEntity<String> getGroupData(@RequestHeader("Authorization") String token) {
        User user = checkUser.check(token);
        Client_team_association client_team_association = repositoryClientTeamAssociation.findByUserId(user.getId());

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
        User user = checkUser.check(token);

        if (user == null) {
            return ResponseEntity.ok(new JSONObject().put("newGroupRequest", false).toString());
        }

        JSONObject newTeamJsonData = new JSONObject(body.toString());
        String teamEmptyPassword = newTeamJsonData.getString("password");

        if (teamEmptyPassword == null) {
            teamEmptyPassword = "";
        }

        Team newTeam = new Team(newTeamJsonData.getString("name"), newTeamJsonData.getString("description"), user.getId(), 
                    newTeamJsonData.getBoolean("public"), teamEmptyPassword, newTeamJsonData.getString("date"));

        Team hasBeenSaved = repositoryTeams.save(newTeam);
        
        if (hasBeenSaved != null) {
            return ResponseEntity.ok(new JSONObject().put("newGroupRequest", true).toString());
        }

        return ResponseEntity.ok(new JSONObject().put("newGroupRequest", false).toString());
    }

    @PostMapping("/getMembers")
    public ResponseEntity<String> getMembers(@RequestHeader("Authorization") String token, @RequestBody String body) {
        User user = checkUser.check(token);

        if (user == null) {
            return ResponseEntity.ok(new JSONObject().put("foundedMembers", false).toString());
        }

        java.util.List<Client_team_association> members = repositoryClientTeamAssociation.findByTeamkey(new JSONObject(body).getString("teamKey"));

        int jsonKeyIncrement = 0;
        JSONObject allMembers = new JSONObject();
        for (Client_team_association c : members) {
            User usr = repositoryUser.findById(c.getUserId());

            boolean isAdmin = false;
            if (usr.getId() == repositoryTeams.findByTeamkey(c.getTeamKey()).getAdministrator()) {
                isAdmin = true;
            }

            boolean isOnline = repositoryUser.findById(c.getUserId()).isLogged_in();

            allMembers.put(String.valueOf(jsonKeyIncrement), new JSONObject()
            .put("username", usr.getUsername())
            .put("groupAdmin", isAdmin)
            .put("online", isOnline));

            jsonKeyIncrement++;
        }

        return ResponseEntity.ok(allMembers.toString());
    }

    @PostMapping("/leaveGroup")
    public ResponseEntity<String> leaveGroup(@RequestHeader("Authorization") String token) {
        User user = checkUser.check(token);

        if (user == null) {
            return ResponseEntity.ok(new JSONObject().put("leavedGroup", false).toString());
        }

        repositoryClientTeamAssociation.deleteById(repositoryClientTeamAssociation.findByUserId(user.getId()).getId());;
        user.setInGroup(false);
        repositoryUser.save(user);

        return ResponseEntity.ok(new JSONObject().put("leavedGroup", true).toString());
    }

    @PostMapping("amiadmin")
    public ResponseEntity<String> amIAdminFromGroup(@RequestHeader("Authorization") String token) {
        User user = checkUser.check(token);
        
        try {
            Team team = repositoryTeams.findByAdministrator(user.getId());

            if (team != null)  {
                return ResponseEntity.ok(new JSONObject().put("areYouAdmin", true).toString());
            }
        } catch (NonUniqueResultException nonUnique) {
            //? LOG: The user is in more than one group
        }

        return ResponseEntity.ok(new JSONObject().put("areYouAdmin", false).toString());
    }

    @PostMapping("/addOrChangePassword")
    public void addOrChangePassword(@RequestHeader("Authorization") String token, @RequestBody String body) {
        User user = checkUser.check(token);
        
        JSONObject request = new JSONObject(body);
        Team team = repositoryTeams.findByTeamkey(request.getString("teamKey"));
        if (user != null && team.getAdministrator() != user.getId()) {
            //? LOG: User request is not an admin
            return;
        }

        team.setPassword(request.getString("newPassword"));
        repositoryTeams.save(team);
    }

    @PostMapping("/updateTeamAdmin")
    public ResponseEntity<String> updateTeamAdmin(@RequestHeader("Authorization") String token, @RequestBody String body) {
        User user = checkUser.check(token);

        JSONObject request = new JSONObject(body);
        Team team = repositoryTeams.findByTeamkey(request.getString("teamKey"));
        if (user != null && team.getAdministrator() != user.getId()) {
            // ? LOG: User request is not an admin
            return ResponseEntity.ok(new JSONObject().put("newAdminModificationStatus", false).toString());
        }

        team.setAdministrator(repositoryUser.findByUsername(request.getString("newAdminUsername")).getId());
        repositoryTeams.save(team);

        return ResponseEntity.ok(new JSONObject().put("newAdminModificationStatus", true).toString());
    }

    @PostMapping("/deleteEmptyGroup")
    public void deleteEmptyGroup(@RequestHeader("Authorization") String token) {
        User user = checkUser.check(token);

        if (user != null && repositoryClientTeamAssociation.getMembersByTeamKey(repositoryTeams.findByAdministrator(user.getId()).getTeamkey()) == 1) {
            Client_team_association toDelete = repositoryClientTeamAssociation.findByUserId(user.getId());
            repositoryClientTeamAssociation.deleteById(toDelete.getId());
            repositoryTeams.deleteById(toDelete.getTeamKey());
            user.setInGroup(false);
            repositoryUser.save(user);
        }
    }

    @PostMapping("/deleteEntireTeam")
    public ResponseEntity<String> deleteEntireTeam(@RequestHeader("Authorization") String token, @RequestBody String body) {
        User user = checkUser.check(token);

        JSONObject delRequest = new JSONObject(body);
        if (user != null && repositoryTeams.findByAdministrator(user.getId()) != null) {
            java.util.List<Client_team_association> clientTeamAssociationList = repositoryClientTeamAssociation.findByTeamkey(delRequest.getString("teamKey"));

            for (Client_team_association c: clientTeamAssociationList) {
                repositoryClientTeamAssociation.deleteById(c.getId());
                User u = repositoryUser.findById(c.getUserId());
                u.setInGroup(false);
                repositoryUser.save(u);
            }

            repositoryTeams.deleteById(delRequest.getString("teamKey"));
            return ResponseEntity.ok(new JSONObject().put("deleteTeamAction", true).toString());
        }

        return ResponseEntity.ok(new JSONObject().put("deleteTeamAction", false).toString());
    } 

    @PostMapping("/kickUser")
    public ResponseEntity<String> kickUser(@RequestHeader("Authorization") String token, @RequestBody String body) {
        User user = checkUser.check(token);
        
        JSONObject request = new JSONObject(body);
        User userToDelete = repositoryUser.findByUsername(request.getString("username"));
        
        if (user != null && repositoryTeams.findByAdministrator(user.getId()) != null && userToDelete != null) {
            // User is admin and user to delete exist
            repositoryClientTeamAssociation.deleteByUserId(userToDelete.getId());
            userToDelete.setInGroup(false);
            repositoryUser.save(userToDelete);
            
            return ResponseEntity.ok(new JSONObject().put("userKicked", true).toString());
        }

        return ResponseEntity.ok(new JSONObject().put("userKicked", false).toString());
    }
}