package com.to_do_api.todo_today_api.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Health {
    @RequestMapping("/health")
    public ResponseEntity<Boolean> healthReq() {
        return ResponseEntity.ok(true);
    }
}
