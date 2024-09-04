package com.to_do_api.todo_today_api.controller.verifyEmailManagement;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.to_do_api.todo_today_api.repo.user.RepositoryUser;
import com.to_do_api.todo_today_api.repo.user.User;
import com.to_do_api.todo_today_api.repo.user_temporal_tokens.RepositoryTemporalTokens;
import com.to_do_api.todo_today_api.repo.verify_email_codes.RepositoryVerifyCodes;
import com.to_do_api.todo_today_api.repo.verify_email_codes.Verify_email_codes;
import com.to_do_api.todo_today_api.services.emailService.EmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.Random;



@RestController
@RequestMapping("verifyEmail")
public class VerifyService {
    @Autowired
    private RepositoryVerifyCodes repositoryVerifyCodes;

    @Autowired
    private RepositoryTemporalTokens repositoryTemporalTokens;

    @Autowired
    private RepositoryUser repositoryUser;

    @Autowired
    private EmailService emailService;

    @PostMapping("/requestVerification")
    public ResponseEntity<String> requestVerification(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");

        User user = repositoryUser.findById(repositoryTemporalTokens.getUserByToken(token).getUserId());

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
        token = token.replace("Bearer ", "");

        User user = repositoryUser.findById(repositoryTemporalTokens.getUserByToken(token).getUserId());

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
