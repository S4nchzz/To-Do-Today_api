package com.to_do_api.todo_today_api.controller.toDoManagement;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.to_do_api.todo_today_api.repo.todo.RepositoryToDos;
import com.to_do_api.todo_today_api.repo.todo.ToDo;
import com.to_do_api.todo_today_api.repo.user_temporal_tokens.RepositoryTemporalTokens;
import com.to_do_api.todo_today_api.repo.user_temporal_tokens.User_temporal_token;

@RestController
@RequestMapping("/toDos")
public class ToDosService {
    @Autowired
    private RepositoryToDos repositoryToDos;

    @Autowired
    private RepositoryTemporalTokens repositoryTemporalTokens;

    @PostMapping("/getToDos")
    public ResponseEntity<?> getToDos(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        try {

            User_temporal_token user_temp_tokens = repositoryTemporalTokens
                    .getUserByToken(token);

            if (user_temp_tokens == null) {
                return ResponseEntity.ok("");
            }

            List<ToDo> toDoList = repositoryToDos.findByUserid(user_temp_tokens.getUserID());
            JSONObject allToDosOnJson = generateJsonWithToDos(toDoList);
            return ResponseEntity.ok(allToDosOnJson.toString());

        } catch (JSONException je) {
            // ? LOG: Token recived is not in JSON type
            return ResponseEntity.ok("Failure authentication");
        }
    }

    private JSONObject generateJsonWithToDos(List<ToDo> list) {
        JSONObject toDosGeneral = new JSONObject();
        for (ToDo ToDos : list) {
            JSONObject toDo = new JSONObject();
            toDo.put("id", ToDos.getId());
            toDo.put("userId", ToDos.getUserid());
            toDo.put("header", ToDos.getHeader());
            toDo.put("content", ToDos.getContent());
            toDo.put("date", ToDos.getDate());
            toDo.put("fav", ToDos.isFav());
            toDo.put("ended", ToDos.isEnded());
            toDo.put("group", ToDos.getGroup());

            toDosGeneral.put(String.valueOf(ToDos.getId()), toDo);
        }

        return toDosGeneral;
    }

    @PostMapping("/addToDo")
    public ResponseEntity<String> addToDo(@RequestBody String values, @RequestHeader("Authorization") String token) {
        try {
            JSONObject toDoData = new JSONObject(values);

            token = token.replace("Bearer ", "");
            User_temporal_token user_temp_tokens = repositoryTemporalTokens
                    .getUserByToken(token);

            if (user_temp_tokens == null) {
                return ResponseEntity.ok(new JSONObject().put("addToDoSucced", false).toString());
            }

            ToDo toDo = new ToDo(user_temp_tokens.getUserID(), toDoData.getString("header"),
                    toDoData.getString("content"), toDoData.getString("date"), toDoData.getBoolean("fav"),
                    toDoData.getBoolean("ended"));
            ToDo providedToDo = repositoryToDos.save(toDo);

            return ResponseEntity.ok(providedToDo.getJson().put("addToDoSucced", true).toString());
        } catch (JSONException e) {
            System.out.println(e.getMessage());
            // ? LOG: Error while generating the new ToDo check token
        }
        return ResponseEntity.ok(new JSONObject().put("addToDoSucced", false).toString());
    }

    @PostMapping("/deleteToDo")
    public ResponseEntity<String> deleteToDo(@RequestBody String body, @RequestHeader("Authorization") String token) {
        JSONObject json = new JSONObject(body);

        token = token.replace("Bearer ", "");

        User_temporal_token user = repositoryTemporalTokens.getUserByToken(token);

        if (user == null) {
            return ResponseEntity.ok(new JSONObject().put("deleted", false).toString());
        }

        repositoryToDos.deleteById(json.getInt("deleteID"));

        return ResponseEntity.ok(new JSONObject().put("deleted", true).toString());
    }

    @PostMapping("/updateToDo")
    public ResponseEntity<String> updateToDo(@RequestBody String body, @RequestHeader("Authorization") String token) {
        JSONObject toDoNewData = new JSONObject(body);
        JSONObject jsonResponse = new JSONObject();

        token = token.replace("Bearer ", "");
        User_temporal_token user_temp_tokens = repositoryTemporalTokens
                .getUserByToken(token);

        if (user_temp_tokens == null) {
            jsonResponse.put("updated", "false");
            return ResponseEntity.ok(jsonResponse.toString());
        }

        repositoryToDos.deleteById(toDoNewData.getInt("id"));
        ToDo todo = repositoryToDos.save(new ToDo(user_temp_tokens.getUserID(), toDoNewData.getString("header"),
                toDoNewData.getString("content"), toDoNewData.getString("date"), toDoNewData.getBoolean("fav"),
                toDoNewData.getBoolean("ended")));

        return ResponseEntity.ok(todo.getJson().put("updated", true).toString());
    }

    @PostMapping("/completeToDo")
    public ResponseEntity<String> completeToDo(@RequestBody String body, @RequestHeader("Authorization") String token) {
        JSONObject json = new JSONObject(body);

        token = token.replace("Bearer ", "");
        User_temporal_token user = repositoryTemporalTokens.findByToken(token);

        if (user != null) {
            ToDo todo = repositoryToDos.findById(json.getInt("id"));

            if (todo != null) {
                todo.setEnded(json.getBoolean("completed"));
                repositoryToDos.save(todo);
            }

            return ResponseEntity.ok(new JSONObject().put("toDoCompletedUpdateResult", true).toString());
        }

        return ResponseEntity.ok(new JSONObject().put("toDoCompletedUpdateResult", false).toString());
    }

    @PostMapping("/addFav")
    public ResponseEntity<String> addFav(@RequestBody String entity, @RequestHeader("Authorization") String token) {
        JSONObject json = new JSONObject(entity.toString());

        token = token.replace("Bearer ", "");
        User_temporal_token user = repositoryTemporalTokens.findByToken(token);

        if (user != null) {
            ToDo todo = repositoryToDos.findById(json.getInt("id"));

            if (todo != null) {
                todo.setFav(json.getBoolean("fav"));
                repositoryToDos.save(todo);

                return ResponseEntity.ok(new JSONObject().put("updated", true).toString());
            }
        }
        return ResponseEntity.ok(new JSONObject().put("updated", false).toString());
    }
}
