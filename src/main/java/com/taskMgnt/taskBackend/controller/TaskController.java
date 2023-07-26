package com.taskMgnt.taskBackend.controller;

import com.taskMgnt.taskBackend.entity.Task;
import com.taskMgnt.taskBackend.payload.TaskDto;
import com.taskMgnt.taskBackend.payload.CreateTaskDto;
import com.taskMgnt.taskBackend.payload.UpdateTaskDto;
import com.taskMgnt.taskBackend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {
    @Autowired
    private TaskService taskService;

    //POST to save task
    @PostMapping("/tasks")
    public ResponseEntity<TaskDto> saveTask(
            @RequestBody CreateTaskDto taskDto
    ){
       return new ResponseEntity<>(taskService.saveTask(taskDto), HttpStatus.CREATED);
    }
    //GET all tasks
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks(
    ){
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }
    //GET individual task
    @GetMapping("/tasks/{taskid}")
    public ResponseEntity<TaskDto> getTask(
        @PathVariable(name = "taskid") long taskid
    ){
        return new ResponseEntity<>(taskService.getTask(taskid),HttpStatus.OK);
    }

    //DELETE individual task
    @DeleteMapping("/tasks/{taskid}")
    public ResponseEntity<String> deleteTask(
            @PathVariable(name = "taskid") long taskid
    ){
        taskService.deleteTask(taskid);
        return new ResponseEntity<>("Task Deleted Succesfully",HttpStatus.OK);
    }
    @PutMapping("/tasks/{taskid}")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable(name = "taskid") long taskid,
            @RequestBody UpdateTaskDto updateTaskDto
    ){
        return new ResponseEntity<>(taskService.updateTask(taskid,updateTaskDto),HttpStatus.OK);
    }
}
