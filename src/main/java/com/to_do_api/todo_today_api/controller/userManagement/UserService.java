package com.to_do_api.todo_today_api.controller.userManagement;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.to_do_api.todo_today_api.repo.user.RepositoryUser;
import com.to_do_api.todo_today_api.userAccount.PasswordComplexity;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/user")
public class UserService {
    @Autowired
    private RepositoryUser repositoryUser;

    @PutMapping("/addUser")
    public ResponseEntity<Boolean> addUser(@RequestBody String entity) {
        System.out.println(entity.toString());
        // Add salt and hashed pass
        JSONObject updatedEntity = new JSONObject(entity);
        PasswordComplexity passComplex = new PasswordComplexity(updatedEntity.getString("password"));

        updatedEntity.put("salt", passComplex.getSalt());
        // Remove plane text password to add hashed password
        updatedEntity.remove("password");
        updatedEntity.put("password", passComplex.getHashedPassword());
        updatedEntity.put("logged_in", false);

        repositoryUser.save(JSONToUser.getUserFromJson(updatedEntity));

        return ResponseEntity.ok(true);
    }
}
