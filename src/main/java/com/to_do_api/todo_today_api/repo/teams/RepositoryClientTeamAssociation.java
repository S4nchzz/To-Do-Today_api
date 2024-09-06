package com.to_do_api.todo_today_api.repo.teams;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.List;


@Repository
public interface RepositoryClientTeamAssociation extends JpaRepository<Client_team_association, Integer>{
    Client_team_association findByUserId(int userId);
    
    @Query("SELECT COUNT(c) from Client_team_association c where c.teamkey = :teamkey")
    Integer getMembersByTeamKey(@Param("teamkey") String teamkey);

    java.util.List<Client_team_association> findByTeamkey(String teamkey);    

    @Transactional
    void deleteByUserId(int userId);
}
