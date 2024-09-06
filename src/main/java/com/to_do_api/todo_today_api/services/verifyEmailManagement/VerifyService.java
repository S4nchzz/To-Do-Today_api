package com.to_do_api.todo_today_api.services.verifyEmailManagement;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.to_do_api.todo_today_api.repo.user.RepositoryUser;
import com.to_do_api.todo_today_api.repo.user.User;
import com.to_do_api.todo_today_api.repo.verify_email_codes.RepositoryVerifyCodes;
import com.to_do_api.todo_today_api.repo.verify_email_codes.Verify_email_codes;
import com.to_do_api.todo_today_api.services.CheckUserExist;
import com.to_do_api.todo_today_api.services.emailService.EmailService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.Random;



@RestController
@RequestMapping("verifyEmail")
public class VerifyService {
    private CheckUserExist checkUser;
    private RepositoryVerifyCodes repositoryVerifyCodes;
    private RepositoryUser repositoryUser;
    private EmailService emailService;

    public VerifyService(RepositoryVerifyCodes repositoryVerifyCodes, RepositoryUser repositoryUser, EmailService emailService, CheckUserExist checkUserExist) {
        this.repositoryVerifyCodes = repositoryVerifyCodes;
        this.repositoryUser = repositoryUser;
        this.emailService = emailService;

        this.checkUser = checkUserExist;
    }

    @PostMapping("/requestVerification")
    public ResponseEntity<String> requestVerification(@RequestHeader("Authorization") String token) {
        User user = checkUser.check(token);

        if (user == null) {
            return ResponseEntity.ok(new JSONObject().put("requestVerificationStatus", false).toString());
        }

        Random random = new Random();
        int verificationCode = 1000 + random.nextInt(9000);

        repositoryVerifyCodes.save(new Verify_email_codes(verificationCode, user.getEmail()));

        // Send email with the code
        emailService.sendEmail(user.getEmail(), "Verification Code / To-Do-Today", "CODE: " + verificationCode);
        return ResponseEntity.ok(new JSONObject().put("requestVerificationStatus", true).toString());
    }

    @PostMapping("/confirmVerification")
    public ResponseEntity<String> confirmVerification(@RequestHeader("Authorization") String token, @RequestBody String body) {
        User user = checkUser.check(token);

        if (user == null) {
            return ResponseEntity.ok(new JSONObject().put("verificationCompletedStatus", false).toString());
        }

        Verify_email_codes checkCode = repositoryVerifyCodes.findVerification(new JSONObject(body).getInt("code"), user.getEmail());

        if (checkCode == null) {
            return ResponseEntity.ok(new JSONObject().put("verificationCompletedStatus", false).toString());
        }

        user.setEmailVerified(true);
        repositoryUser.save(user);
        return ResponseEntity.ok(new JSONObject().put("verificationCompletedStatus", true).toString());
    }
}
