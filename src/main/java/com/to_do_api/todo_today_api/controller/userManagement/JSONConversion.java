package com.to_do_api.todo_today_api.controller.userManagement;

import org.json.JSONException;
import org.json.JSONObject;

import com.to_do_api.todo_today_api.repo.user.User;

public class JSONConversion {
    public static User getUserFromJson(JSONObject json) {
        try {
            User user = new User(json.getString("username"), json.getString("salt"), (byte [])json.get("password"), json.getString("email"), json.getBoolean("logged_in"));
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
