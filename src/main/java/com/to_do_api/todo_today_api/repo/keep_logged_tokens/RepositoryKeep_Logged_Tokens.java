package com.to_do_api.todo_today_api.repo.keep_logged_tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryKeep_Logged_Tokens extends JpaRepository<Keep_Logged_Tokens, String> {
    Keep_Logged_Tokens findByUserid(int userid);
    Keep_Logged_Tokens findByToken(String string);
}