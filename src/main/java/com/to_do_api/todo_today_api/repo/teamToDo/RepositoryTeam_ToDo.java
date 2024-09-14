package com.to_do_api.todo_today_api.repo.teamToDo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryTeam_ToDo extends JpaRepository<Team_ToDo, Integer> {
    List<Team_ToDo> findByTeamkey(String teamkey);
    Team_ToDo findById(int id);
    void deleteById(int id);
}
