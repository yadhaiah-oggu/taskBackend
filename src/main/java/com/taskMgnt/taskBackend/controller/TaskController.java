package com.taskMgnt.taskBackend.controller;

import com.taskMgnt.taskBackend.payload.TaskDto;
import com.taskMgnt.taskBackend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {
    @Autowired
    private TaskService taskService;

    //POST to save task
    @PostMapping("/{userid}/tasks")
    public ResponseEntity<TaskDto> saveTask(
            @PathVariable(name = "userid") long userid,
            @RequestBody TaskDto taskDto
    ){
       return new ResponseEntity<>(taskService.saveTask(userid, taskDto), HttpStatus.CREATED);
    }
    //GET all tasks
    @GetMapping("/{userid}/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks(
            @PathVariable(name = "userid") long userid
    ){
        return new ResponseEntity<>(taskService.getAllTasks(userid), HttpStatus.OK);
    }
    //GET individual task
    @GetMapping("/{userid}/tasks/{taskid}")
    public ResponseEntity<TaskDto> getTask(
        @PathVariable(name = "userid") long userid,
        @PathVariable(name = "taskid") long taskid
    ){
        return new ResponseEntity<>(taskService.getTask(userid,taskid),HttpStatus.OK);
    }

    //DELETE individual task
    @DeleteMapping("/{userid}/tasks/{taskid}")
    public ResponseEntity<String> deleteTask(
            @PathVariable(name = "userid") long userid,
            @PathVariable(name = "taskid") long taskid
    ){
        taskService.deleteTask(userid,taskid);
        return new ResponseEntity<>("Task Deleted Succesfully",HttpStatus.OK);
    }
}
