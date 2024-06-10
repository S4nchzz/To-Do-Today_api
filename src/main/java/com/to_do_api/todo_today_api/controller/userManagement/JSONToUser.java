package com.to_do_api.todo_today_api.controller.userManagement;

import org.json.JSONObject;

import com.to_do_api.todo_today_api.repo.user.User;

public class JSONToUser {
    public static User getUserFromJson(JSONObject json) {
        User user = new User(json.getString("username"), json.getString("salt"), json.getString("password"), json.getString("email"), json.getBoolean("logged_in"));
        return user;
    }
}
