package com.to_do_api.todo_today_api.repo.to_dos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryTo_Dos extends JpaRepository<To_Dos, Integer>{
    List<To_Dos> findByUserid(int userid);
}