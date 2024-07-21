package com.to_do_api.todo_today_api.controller.toDoManagement;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.to_do_api.todo_today_api.repo.to_dos.RepositoryToDos;
import com.to_do_api.todo_today_api.repo.to_dos.ToDo;
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
    public ResponseEntity<?> getToDos(@RequestBody String body) {
        try {
            JSONObject userToken = new JSONObject(body);

            User_temporal_token user_temp_tokens = repositoryTemporalTokens.getUserByToken(userToken.getString("userToken"));

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

            toDosGeneral.put(String.valueOf(ToDos.getId()), toDo);
        }

        return toDosGeneral;
    }

    @PostMapping("/addToDo")
    public ResponseEntity<String> addToDo(@RequestBody String token) {
        try {
            JSONObject userToken = new JSONObject(token);
            
            User_temporal_token user_temp_tokens = repositoryTemporalTokens
                    .getUserByToken(userToken.getString("userToken"));

            if (user_temp_tokens == null) {
                return ResponseEntity.ok(new JSONObject().put("addToDoSucced", false).toString());
            }

            ToDo toDo = new ToDo(user_temp_tokens.getUserID(), userToken.getString("header"), userToken.getString("content"), userToken.getString("date"), userToken.getBoolean("fav"), userToken.getBoolean("ended"));            
            ToDo providedToDo = repositoryToDos.save(toDo);

            return ResponseEntity.ok(providedToDo.getJson().put("addToDoSucced", true).toString());
        } catch (JSONException e) {
            System.out.println(e.getMessage());
            //? LOG: Error while generating the new ToDo check token
        } 
        return ResponseEntity.ok(new JSONObject().put("addToDoSucced", false).toString());
    }

    @PostMapping("/updateToDo")
    public ResponseEntity<String> updateToDo(@RequestBody String body) {
        JSONObject toDoNewData = new JSONObject(body);
        JSONObject jsonResponse = new JSONObject();
        
        User_temporal_token user_temp_tokens = repositoryTemporalTokens
                .getUserByToken(toDoNewData.getString("userToken"));

        if (user_temp_tokens == null) {
            jsonResponse.put("updated", "false");
            return ResponseEntity.ok(jsonResponse.toString());
        }
        
        repositoryToDos.deleteById(toDoNewData.getInt("id"));
        ToDo todo = repositoryToDos.save(new ToDo(user_temp_tokens.getUserID(), toDoNewData.getString("header"), toDoNewData.getString("content"), toDoNewData.getString("date"), toDoNewData.getBoolean("fav"), toDoNewData.getBoolean("ended")));

        return ResponseEntity.ok(todo.getJson().put("updated", true).toString());
    }

    @PostMapping("/completeToDo")
    public ResponseEntity<String> completeToDo(@RequestBody String body) {
        JSONObject json = new JSONObject(body);

        User_temporal_token user = repositoryTemporalTokens.findByToken(json.getString("userToken"));

        if (user != null) {
            repositoryToDos.deleteById(json.getInt("id"));
            repositoryToDos.save(new ToDo(user.getUserID(), json.getString("header"), json.getString("content"), json.getString("date"), json.getBoolean("fav"), true));
            
            JSONObject responseJson = new JSONObject();
            responseJson.put("toDoCompletedUpdateResult", true);
            return ResponseEntity.ok(responseJson.toString());
        }

        JSONObject responseJson = new JSONObject();
        responseJson.put("toDoCompletedUpdateResult", false);
        return ResponseEntity.ok(responseJson.toString());
    }
    
}
