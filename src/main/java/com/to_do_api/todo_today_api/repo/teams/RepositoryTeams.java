package com.to_do_api.todo_today_api.repo.teams;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RepositoryTeams extends JpaRepository<Team, String>{
    Team findByTeamkey(String teamkey);
    List<Team> findAll();
    Team findByAdministrator(int administrator);
}
