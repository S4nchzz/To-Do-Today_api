package com.to_do_api.todo_today_api.services;

import org.springframework.stereotype.Component;

import com.to_do_api.todo_today_api.repo.user.RepositoryUser;
import com.to_do_api.todo_today_api.repo.user.User;
import com.to_do_api.todo_today_api.repo.user_temporal_tokens.RepositoryTemporalTokens;
import com.to_do_api.todo_today_api.repo.user_temporal_tokens.User_temporal_token;

@Component
public class CheckUserExist {
    private RepositoryTemporalTokens repositoryTemporalTokens;
    private RepositoryUser repositoryUser;

    public CheckUserExist(RepositoryTemporalTokens repositoryTemporalTokens, RepositoryUser repositoryUser) {
        this.repositoryTemporalTokens = repositoryTemporalTokens;
        this.repositoryUser = repositoryUser;
    }

    public User check(String token) {
        token = token.replace("Bearer ", "");

        User_temporal_token user = repositoryTemporalTokens.findByToken(token);

        return repositoryUser.findById(user.getUserId());
    }
}