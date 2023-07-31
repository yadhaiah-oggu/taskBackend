package com.taskMgnt.taskBackend.service;

import com.taskMgnt.taskBackend.payload.TaskDto;
import com.taskMgnt.taskBackend.payload.CreateTaskDto;
import com.taskMgnt.taskBackend.payload.UpdateTaskDto;

import java.util.List;

public interface TaskService {
    public TaskDto saveTask(CreateTaskDto taskDto);
    public List<TaskDto> getAllTasks();
    public TaskDto getTask(long taskid);
    public TaskDto deleteTask(long taskid);
    public TaskDto updateTask(long taskid, UpdateTaskDto taskDto);

}
