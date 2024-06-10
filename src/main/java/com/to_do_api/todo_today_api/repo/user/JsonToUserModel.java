package com.to_do_api.todo_today_api.repo.user;

import org.json.JSONObject;

public class JsonToUserModel {
    private User userModel;
    public JsonToUserModel (String jsonOnString) {
        User userModel = new User();
        JSONObject json = new JSONObject(jsonOnString);
        userModel.setId((int)json.get("id"));
        userModel.setUsername((String)json.get("username"));
        userModel.setSalt((String) json.get("salt"));
        userModel.setPass((String) json.get("pass"));
        userModel.setEmail((String) json.get("email"));
        userModel.setLogged_in((boolean) json.get("logged_in"));

        this.userModel = userModel;
    }

    public User getUserModel () {
        return this.userModel;
    }
}
