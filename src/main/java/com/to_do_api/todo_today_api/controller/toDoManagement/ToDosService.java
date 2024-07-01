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
import com.to_do_api.todo_today_api.repo.to_dos.To_Dos;
import com.to_do_api.todo_today_api.repo.user_temp_tokens.RepositoryAuthTempTokens;
import com.to_do_api.todo_today_api.repo.user_temp_tokens.User_temp_tokens;

@RestController
@RequestMapping("/toDos")
public class ToDosService {
    @Autowired
    private RepositoryTo_Dos repositoryTo_Dos;

    @Autowired
    private RepositoryAuthTempTokens repositoryAuthTempTokens;

    @PostMapping("/getToDos")
    public ResponseEntity<?> getToDos(@RequestBody String body) {
        try {
            JSONObject userToken = new JSONObject(body);

            User_temp_tokens user_temp_tokens = repositoryAuthTempTokens.getUserByToken(userToken.getString("userToken"));

            if (user_temp_tokens == null) {
                return ResponseEntity.ok("");
            }

            List<To_Dos> toDoList = repositoryTo_Dos.findByUserid(user_temp_tokens.getUserID());
            JSONObject allToDosOnJson = generateJsonWithToDos(toDoList);
            return ResponseEntity.ok(allToDosOnJson.toString());

        } catch (JSONException je) {
            // ? LOG: Token recived is not in JSON type
            return ResponseEntity.ok("Failure authentication");
        }
    }

    private JSONObject generateJsonWithToDos(List<To_Dos> list) {
        JSONObject toDosGeneral = new JSONObject();
        for (To_Dos to_dos : list) {
            JSONObject toDo = new JSONObject();
            toDo.put("User_Id", to_dos.getUserid());
            toDo.put("Header", to_dos.getHeader());
            toDo.put("Content", to_dos.getContent());
            toDo.put("Date", to_dos.getDate());
            toDo.put("Fav", to_dos.isFav());

            toDosGeneral.put(String.valueOf(to_dos.getId()), toDo);
        }

        return toDosGeneral;
    }

    @PostMapping("/addToDo")
    public ResponseEntity<Boolean> postMethodName(@RequestBody String token) {
        try {
            JSONObject userToken = new JSONObject(token);
            
            User_temp_tokens user_temp_tokens = repositoryAuthTempTokens
                    .getUserByToken(userToken.getString("userToken"));

            if (user_temp_tokens == null) {
                return ResponseEntity.ok(false);
            }

            repositoryTo_Dos.save(new To_Dos(user_temp_tokens.getUserID(), userToken.getString("header"), userToken.getString("content"), userToken.getBoolean("fav")));
            return ResponseEntity.ok(true);
        } catch (JSONException e) {
            System.out.println(e.getMessage());
            //? LOG: Error while new ToDo check token
        } 
        return ResponseEntity.ok(false);
    }
}
