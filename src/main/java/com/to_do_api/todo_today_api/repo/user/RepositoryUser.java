package com.to_do_api.todo_today_api.repo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryUser extends JpaRepository<User, Integer>{
    User findById(int id);
    User findByUsername(String username);
}