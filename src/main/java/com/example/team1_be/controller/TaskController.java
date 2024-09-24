package com.example.team1_be.controller;

import com.example.team1_be.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/project")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @DeleteMapping("/{project_id}/task/{task_id}")
    public void createTask(@PathVariable Long project_id, Long task_id) {
//        taskService.deleteTask(project_id, task_id);
    }
}