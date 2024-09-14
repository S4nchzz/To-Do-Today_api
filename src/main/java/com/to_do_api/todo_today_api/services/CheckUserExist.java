package com.to_do_api.todo_today_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.to_do_api.todo_today_api.repo.user.RepositoryUser;
import com.to_do_api.todo_today_api.repo.user.User;
import com.to_do_api.todo_today_api.repo.user_temporal_tokens.RepositoryTemporalTokens;
import com.to_do_api.todo_today_api.repo.user_temporal_tokens.User_temporal_token;

@Component
public class CheckUserExist {
    @Autowired
    private RepositoryTemporalTokens repositoryTemporalTokens;

    @Autowired
    private RepositoryUser repositoryUser;

    public CheckUserExist() {
        
    }

    public User check(String token) {
        token = token.replace("Bearer ", "");

        User_temporal_token user = repositoryTemporalTokens.findByToken(token);

        return repositoryUser.findById(user.getUserId());
    }
}