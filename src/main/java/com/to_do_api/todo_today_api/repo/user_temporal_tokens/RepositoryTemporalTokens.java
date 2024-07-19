package com.to_do_api.todo_today_api.repo.user_temporal_tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface RepositoryTemporalTokens extends JpaRepository<User_temporal_token, String> {
    @Query("SELECT u FROM User_temporal_token u WHERE u.token = :tkn")
    User_temporal_token getUserByToken(@Param("tkn") String token);
    User_temporal_token findByToken(String token);
}
