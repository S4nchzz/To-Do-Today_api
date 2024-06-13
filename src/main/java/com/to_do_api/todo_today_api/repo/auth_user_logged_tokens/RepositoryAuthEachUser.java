package com.to_do_api.todo_today_api.repo.auth_user_logged_tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryAuthEachUser extends JpaRepository<User_temp_tokens, String> {
}
