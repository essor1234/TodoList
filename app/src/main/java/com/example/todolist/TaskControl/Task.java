package com.example.todolist.TaskControl;

import java.util.List;

public class Task {
    public String title;
    public Boolean isCompleted;
    public List<SubTask> subTasks;
    public Task(String title, List<SubTask> subTasks) {
        this.title = title;
        this.subTasks = subTasks;
        this.isCompleted = false;
    }
}
