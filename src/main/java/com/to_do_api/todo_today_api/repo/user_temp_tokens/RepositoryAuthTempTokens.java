package com.to_do_api.todo_today_api.repo.user_temp_tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface RepositoryAuthTempTokens extends JpaRepository<User_temp_tokens, String> {
    @Query("SELECT u FROM User_temp_tokens u WHERE u.token = :tkn")
    User_temp_tokens getUserByToken(@Param("tkn") String token);
    User_temp_tokens findByToken(String token);
}
