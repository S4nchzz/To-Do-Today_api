package com.to_do_api.todo_today_api.controller.toDoManagement;

public record To_Do_Data(int getId, int getUserId, String getHeader, String getContent, String getDate, boolean getFav) { 
}