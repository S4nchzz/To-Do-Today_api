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

import com.to_do_api.todo_today_api.repo.to_dos.RepositoryTo_Dos;
import com.to_do_api.todo_today_api.repo.to_dos.To_Do;
import com.to_do_api.todo_today_api.repo.user_temporal_tokens.RepositoryTemporalTokens;
import com.to_do_api.todo_today_api.repo.user_temporal_tokens.User_temporal_token;

@RestController
@RequestMapping("/toDos")
public class ToDosService {
    @Autowired
    private RepositoryTo_Dos repositoryTo_Dos;

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

            List<To_Do> toDoList = repositoryTo_Dos.findByUserid(user_temp_tokens.getUserID());
            JSONObject allToDosOnJson = generateJsonWithToDos(toDoList);
            return ResponseEntity.ok(allToDosOnJson.toString());

        } catch (JSONException je) {
            // ? LOG: Token recived is not in JSON type
            return ResponseEntity.ok("Failure authentication");
        }
    }

    private JSONObject generateJsonWithToDos(List<To_Do> list) {
        JSONObject toDosGeneral = new JSONObject();
        for (To_Do to_dos : list) {
            JSONObject toDo = new JSONObject();
            toDo.put("id", to_dos.getId());
            toDo.put("userId", to_dos.getUserid());
            toDo.put("header", to_dos.getHeader());
            toDo.put("content", to_dos.getContent());
            toDo.put("date", to_dos.getDate());
            toDo.put("fav", to_dos.isFav());
            toDo.put("ended", to_dos.isEnded());

            toDosGeneral.put(String.valueOf(to_dos.getId()), toDo);
        }

        return toDosGeneral;
    }

    // ! Codigo temporal para hacer pruebas
    @PostMapping("/addToDo")
    public ResponseEntity<String> postMethodName(@RequestBody String token) {
        try {
            JSONObject userToken = new JSONObject(token);
            
            User_temporal_token user_temp_tokens = repositoryTemporalTokens
                    .getUserByToken(userToken.getString("userToken"));

            if (user_temp_tokens == null) {
                return ResponseEntity.ok(new JSONObject().put("addToDoSucced", false).toString());
            }

            To_Do toDo = new To_Do(user_temp_tokens.getUserID(), userToken.getString("header"), userToken.getString("content"), userToken.getBoolean("fav"), false);
            repositoryTo_Dos.save(toDo);

            return ResponseEntity.ok(new JSONObject().put("addToDoSucced", true).toString());
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
        
        repositoryTo_Dos.deleteById(toDoNewData.getInt("id"));
        repositoryTo_Dos.save(new To_Do(user_temp_tokens.getUserID(), toDoNewData.getString("header"), toDoNewData.getString("content"), toDoNewData.getBoolean("fav"), false));
        
        jsonResponse.put("updated", "true");
        return ResponseEntity.ok(jsonResponse.toString());
    }

    @PostMapping("/completeToDo")
    public ResponseEntity<String> completeToDo(@RequestBody String body) {
        JSONObject json = new JSONObject(body);

        User_temporal_token user = repositoryTemporalTokens.findByToken(json.getString("userToken"));

        if (user != null) {
            repositoryTo_Dos.deleteById(json.getInt("id"));
            repositoryTo_Dos.save(new To_Do(user.getUserID(), json.getString("header"), json.getString("content"), json.getBoolean("fav"), true));
            
            JSONObject responseJson = new JSONObject();
            responseJson.put("toDoCompletedUpdateResult", true);
            return ResponseEntity.ok(responseJson.toString());
        }

        JSONObject responseJson = new JSONObject();
        responseJson.put("toDoCompletedUpdateResult", false);
        return ResponseEntity.ok(responseJson.toString());
    }
    
}
