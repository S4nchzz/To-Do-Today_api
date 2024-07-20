package com.to_do_api.todo_today_api.repo.to_dos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryToDos extends JpaRepository<ToDo, Integer>{
    List<ToDo> findByUserid(int userid);
    Boolean deleteById(int id);
}