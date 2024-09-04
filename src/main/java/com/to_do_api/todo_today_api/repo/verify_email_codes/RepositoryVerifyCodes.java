package com.to_do_api.todo_today_api.repo.verify_email_codes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryVerifyCodes extends JpaRepository<Verify_email_codes, Integer>{
    Verify_email_codes findByCode(int code);
    @Query("SELECT v FROM Verify_email_codes v WHERE v.code = :code AND v.email = :email")
    Verify_email_codes findVerification(@Param("code") int code, @Param("email") String email);
}
