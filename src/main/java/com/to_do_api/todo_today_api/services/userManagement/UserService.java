package com.to_do_api.todo_today_api.services.userManagement;

import java.security.MessageDigest;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.to_do_api.todo_today_api.repo.auth_api_tokens.RepositoryTokens;
import com.to_do_api.todo_today_api.repo.keep_logged_tokens.Keep_Logged_Tokens;
import com.to_do_api.todo_today_api.repo.keep_logged_tokens.RepositoryKeep_Logged_Tokens;
import com.to_do_api.todo_today_api.repo.user.RepositoryUser;
import com.to_do_api.todo_today_api.repo.user.User;
import com.to_do_api.todo_today_api.repo.user_temporal_tokens.RepositoryTemporalTokens;
import com.to_do_api.todo_today_api.repo.user_temporal_tokens.User_temporal_token;
import com.to_do_api.todo_today_api.services.CheckUserExist;
import com.to_do_api.todo_today_api.userAccount.PasswordComplexity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;



@RestController
@RequestMapping("/user")
public class UserService {
    private CheckUserExist checkUser;
    private RepositoryUser repositoryUser;
    private RepositoryTokens repositoryTokens;
    private RepositoryTemporalTokens repositoryTemporalTokens;
    private RepositoryKeep_Logged_Tokens repositoryKeep_Logged_Tokens;
    
    public UserService (RepositoryUser repositoryUser, RepositoryTokens repositoryTokens, RepositoryTemporalTokens repositoryTemporalTokens, RepositoryKeep_Logged_Tokens repositoryKeep_Logged_Tokens, CheckUserExist checkUserExist) {
        this.repositoryUser = repositoryUser;
        this.repositoryTokens = repositoryTokens;
        this.repositoryTemporalTokens = repositoryTemporalTokens;
        this.repositoryKeep_Logged_Tokens = repositoryKeep_Logged_Tokens;
    
        this.checkUser = checkUserExist;
    }

    public boolean localAuth(String token) {
        token = token.replace("Bearer ", "");
        if (repositoryTokens.findByToken(token) != null) {
            return true;
        }

        return false;
    }

    @PostMapping("/addUser")
    public ResponseEntity<Boolean> addUser(@RequestBody String entity) {
        System.out.println(entity.toString());
        // Add salt and hashed pass
        JSONObject updatedEntity = new JSONObject(entity);
        PasswordComplexity passComplex = new PasswordComplexity(updatedEntity.getString("password"));

        updatedEntity.put("salt", passComplex.getSalt());
        updatedEntity.put("password", passComplex.getHashedPasswordByte());
        updatedEntity.put("logged_in", false);

        try {
            repositoryUser.save(JSONConversion.getUserFromJson(updatedEntity));
        } catch (Exception e) {
            // ? LOG: Failed to add user
            return ResponseEntity.ok(false);
        }

        return ResponseEntity.ok(true);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> entry, @RequestHeader("Authorization") String token) {
        User user = repositoryUser.findByUsername((String)entry.get("username"));
        
        if (user == null) {
            JSONObject userExist = new JSONObject();
            userExist.put("succed", "false");
            return ResponseEntity.ok(userExist.toString());
        }

        PasswordComplexity passwordComplexity = new PasswordComplexity(user.getSalt(), (String)entry.get("password"));
 
        if (MessageDigest.isEqual(passwordComplexity.getHashedPasswordByte(), user.getPass()))  {
            String authEachUserToken = insertIntoUserTempTokens(user.getId());

            JSONObject tempUserAuth = new JSONObject();
            tempUserAuth.put("succed", "true");
            tempUserAuth.put("tempUserAuthTkn", authEachUserToken);
            return ResponseEntity.ok(tempUserAuth.toString());
        }

        JSONObject userExist = new JSONObject();
        userExist.put("succed", "false");
        return ResponseEntity.ok(userExist.toString());
    }

    private String insertIntoUserTempTokens(int userId) {
        UUID uuid = UUID.randomUUID();
        this.repositoryTemporalTokens.save(new User_temporal_token(uuid.toString(), userId));

        return uuid.toString();
    }

    @PostMapping("/generateUserTempToken")
    public ResponseEntity<String> generateUserTempToken(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        
        Keep_Logged_Tokens keep_Logged_Tokens = this.repositoryKeep_Logged_Tokens.findByToken(token);
        String uuid = insertIntoUserTempTokens(keep_Logged_Tokens.getUserid());

        return ResponseEntity.ok(uuid.toString());
    }
    

    @PostMapping("/generateKeepLoggedTkn")
    public ResponseEntity<?> generateKeepLoggedToken (@RequestHeader("Authorization") String token) {
        UUID uuid = UUID.randomUUID();

        User user = checkUser.check(token);

        if (user == null) {
            return ResponseEntity.ok("");
        }

        repositoryKeep_Logged_Tokens.save(new Keep_Logged_Tokens(uuid.toString(), user.getId()));
        
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("KeepLoggedToken", uuid.toString());
        
        return ResponseEntity.ok(jsonResponse.toString());
    }

    @PostMapping("/checkKeepLoggedTkn")
    public ResponseEntity<Boolean> checkKeepLogggedTkn(@RequestBody String keepLoggedJsonToken) {
        JSONObject json = new JSONObject(keepLoggedJsonToken);

        Keep_Logged_Tokens obj = repositoryKeep_Logged_Tokens.findByToken(json.getString("keepLoggedToken")); 

        return obj == null ? ResponseEntity.ok(false) : ResponseEntity.ok(true);
    }

    @PostMapping("/getUserName")
    public ResponseEntity<String> getUserName(@RequestHeader("Authorization") String token) {
        User user = checkUser.check(token);

        return ResponseEntity.ok(new JSONObject().put("username", user.getUsername()).toString());
    }

    @PostMapping("/isInGroup")
    public ResponseEntity<String> isUserInGroup(@RequestHeader("Authorization") String token) {
        User user = checkUser.check(token);

        if (user != null) {
            return ResponseEntity.ok(new JSONObject().put("hasGroup", user.isInGroup()).toString());
        }

        return ResponseEntity.ok(new JSONObject().put("hasGroup", false).toString());
    }

    @PostMapping("/isEmailVerified")
    public ResponseEntity<String> isEmailVerified(@RequestHeader("Authorization") String token) {
        User user = checkUser.check(token);

        if (user != null) {
            return ResponseEntity.ok(new JSONObject().put("isEmailVerified", user.isEmailVerified()).toString());
        }

        return ResponseEntity.ok(new JSONObject().put("isEmailVerified", false).toString());
    }

    @PostMapping("/getEmail")
    public ResponseEntity<String> getEmail(@RequestHeader("Authorization") String token) {
        User user = checkUser.check(token);

        if (user != null) {
            return ResponseEntity.ok(new JSONObject().put("email", user.getEmail()).toString());
        }

        return ResponseEntity.ok(new JSONObject().put("email", "null").toString());
    }
    
    @PostMapping("/setOnlineUser")
    public void postMethodName(@RequestHeader("Authorization") String token) {
        User user = checkUser.check(token);

        if (user != null) {
            user.setLogged_in(true);
            repositoryUser.save(user);
        }
    }
}