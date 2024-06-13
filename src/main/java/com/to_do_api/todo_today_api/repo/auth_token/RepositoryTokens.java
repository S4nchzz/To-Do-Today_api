package com.to_do_api.todo_today_api.repo.auth_token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryTokens extends JpaRepository<Auth_Api_Tokens, Integer>{
    Auth_Api_Tokens findByToken(String token);
}
