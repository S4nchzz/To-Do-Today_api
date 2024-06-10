package com.to_do_api.todo_today_api.repo.user;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users")
public class UserService {
    private User userModel;
    private final RepositoryUser repositoryUser;
    
    @Autowired
    public UserService(RepositoryUser repositoryUser) {
        this.repositoryUser = repositoryUser;
    }

    public UserService(User userModel, RepositoryUser repositoryUser) {
        this.userModel = userModel;
        this.repositoryUser = repositoryUser;
    }

    @PostMapping("/addUser")
    public ResponseEntity<Boolean> postMethodName(@RequestBody String userModel) {
        System.out.println(userModel.toString());
        JsonToUserModel jsonToUserModel = new JsonToUserModel(userModel);
        repositoryUser.save(jsonToUserModel.getUserModel());

        return ResponseEntity.ok(true);
    }
}
