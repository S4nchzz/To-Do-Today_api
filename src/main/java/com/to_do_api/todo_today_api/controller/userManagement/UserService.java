package com.to_do_api.todo_today_api.controller.userManagement;

import java.security.MessageDigest;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.to_do_api.todo_today_api.repo.auth_api_tokens.RepositoryTokens;
import com.to_do_api.todo_today_api.repo.keep_logged_tokens.Keep_Logged_Tokens;
import com.to_do_api.todo_today_api.repo.keep_logged_tokens.RepositoryKeep_Logged_Tokens;
import com.to_do_api.todo_today_api.repo.user.RepositoryUser;
import com.to_do_api.todo_today_api.repo.user.User;
import com.to_do_api.todo_today_api.repo.user_temp_tokens.RepositoryAuthTempTokens;
import com.to_do_api.todo_today_api.repo.user_temp_tokens.User_temp_tokens;
import com.to_do_api.todo_today_api.userAccount.PasswordComplexity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/user")
public class UserService {
    @Autowired
    private RepositoryUser repositoryUser;

    @Autowired
    private RepositoryTokens repositoryTokens;

    @Autowired
    private RepositoryAuthTempTokens repositoryAuthTempTokens;

    @Autowired
    private RepositoryKeep_Logged_Tokens repositoryKeep_Logged_Tokens;
    
    public UserService () {
    }

    public boolean localAuth(String token) {
        token = token.replace("Bearer ", "");
        if (repositoryTokens.findByToken(token) != null) {
            return true;
        }

        return false;
    }

    @PostMapping("/addUser")
    public ResponseEntity<Boolean> addUser(@RequestBody String entity, @RequestHeader("Authorization") String token) {
        if (!localAuth(token)) {
            return ResponseEntity.ok(false);
        }

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
        if (!localAuth(token)) {
            return ResponseEntity.ok(false);
        }

        User user = repositoryUser.findByUsername((String)entry.get("username"));
        
        if (user == null) {
            return ResponseEntity.ok(false);
        }

        PasswordComplexity passwordComplexity = new PasswordComplexity(user.getSalt(), (String)entry.get("password"));
 
        if (MessageDigest.isEqual(passwordComplexity.getHashedPasswordByte(), user.getPass()))  {
            String authEachUserToken = insertIntoUserTempTokens(user.getId());

            JSONObject tempUserAuth = new JSONObject();
            tempUserAuth.put("tempUserAuthTkn", authEachUserToken);
            return ResponseEntity.ok(tempUserAuth.toString());
        }

        return ResponseEntity.ok(false);
    }

    private String insertIntoUserTempTokens(int userId) {
        UUID uuid = UUID.randomUUID();
        this.repositoryAuthTempTokens.save(new User_temp_tokens(uuid.toString(), userId));

        return uuid.toString();
    }

    @PostMapping("/generateUserTempToken")
    public ResponseEntity<String> postMethodName(@RequestBody String userKeepLoggedUserTkn) {
        Keep_Logged_Tokens keep_Logged_Tokens = this.repositoryKeep_Logged_Tokens.findByToken(userKeepLoggedUserTkn);
        String uuid = insertIntoUserTempTokens(keep_Logged_Tokens.getUserid());

        return ResponseEntity.ok(uuid.toString());
    }
    

    @PostMapping("/generateKeepLoggedTkn")
    public ResponseEntity<?> generateKeepLoggedToken (@RequestHeader("Authorization") String token) {
        UUID uuid = UUID.randomUUID();

        token = token.replace("Bearer ", "");
        User_temp_tokens user_temp_tokens = repositoryAuthTempTokens.getUserByToken(token);

        if (user_temp_tokens == null) {
            return ResponseEntity.ok("");
        }

        repositoryKeep_Logged_Tokens.save(new Keep_Logged_Tokens(uuid.toString(), user_temp_tokens.getUserID()));
        
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("KeepLoggedToken", uuid.toString());
        
        return ResponseEntity.ok(jsonResponse.toString());
    }

    @PostMapping("/checkKeepLoggedTkn")
    public ResponseEntity<Boolean> checkKeepLogggedTkn(@RequestBody String keepLoggedJsonToken, @RequestHeader("Authorization") String authToken) {
        JSONObject json = new JSONObject(keepLoggedJsonToken);

        Keep_Logged_Tokens obj = repositoryKeep_Logged_Tokens.findByToken(json.getString("keepLoggedToken")); 

        return obj == null ? ResponseEntity.ok(false) : ResponseEntity.ok(true);
    }

    @PostMapping("/getUserName")
    public ResponseEntity<String> getUserName(@RequestBody String body) {
        JSONObject json = new JSONObject(body);

        User_temp_tokens user_temp_tokens = this.repositoryAuthTempTokens.findByToken(json.getString("userTempToken"));
        User user = this.repositoryUser.findById(user_temp_tokens.getUserID());

        return ResponseEntity.ok(user.getUsername());
    }
}