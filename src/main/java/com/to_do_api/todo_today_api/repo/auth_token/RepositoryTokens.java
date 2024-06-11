package com.to_do_api.todo_today_api.repo.auth_token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryTokens extends JpaRepository<Auth_Tokens, Integer>{
    Auth_Tokens findByToken(String token);
}
