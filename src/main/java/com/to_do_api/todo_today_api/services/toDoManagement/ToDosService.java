package com.to_do_api.todo_today_api.services.toDoManagement;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.to_do_api.todo_today_api.repo.todo.RepositoryToDos;
import com.to_do_api.todo_today_api.repo.todo.ToDo;
import com.to_do_api.todo_today_api.repo.user.User;
import com.to_do_api.todo_today_api.services.CheckUserExist;

@RestController
@RequestMapping("/toDos")
public class ToDosService {
    private final CheckUserExist checkUser;
    private RepositoryToDos repositoryToDos;

    public ToDosService(RepositoryToDos repositoryToDos, CheckUserExist checkUserExist) {
        this.checkUser = checkUserExist;

        this.repositoryToDos = repositoryToDos;
    }

    @PostMapping("/getToDos")
    public ResponseEntity<?> getToDos(@RequestHeader("Authorization") String token) {
        User user = checkUser.check(token);

        if (user == null) {
            return ResponseEntity.ok("");
        }

        List<ToDo> toDoList = repositoryToDos.findByUserid(user.getId());
        JSONObject allToDosOnJson = generateJsonWithToDos(toDoList);
        return ResponseEntity.ok(allToDosOnJson.toString());
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

            User user = checkUser.check(token);

            if (user == null) {
                return ResponseEntity.ok(new JSONObject().put("addToDoSucced", false).toString());
            }

            ToDo toDo = new ToDo(user.getId(), toDoData.getString("header"),
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

        User user = checkUser.check(token);

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

        User user = checkUser.check(token);

        if (user == null) {
            jsonResponse.put("updated", "false");
            return ResponseEntity.ok(jsonResponse.toString());
        }

        repositoryToDos.deleteById(toDoNewData.getInt("id"));
        ToDo todo = repositoryToDos.save(new ToDo(user.getId(), toDoNewData.getString("header"),
                toDoNewData.getString("content"), toDoNewData.getString("date"), toDoNewData.getBoolean("fav"),
                toDoNewData.getBoolean("ended")));

        return ResponseEntity.ok(todo.getJson().put("updated", true).toString());
    }

    @PostMapping("/completeToDo")
    public ResponseEntity<String> completeToDo(@RequestBody String body, @RequestHeader("Authorization") String token) {
        JSONObject json = new JSONObject(body);

        User user = checkUser.check(token);

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

        User user = checkUser.check(token);

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