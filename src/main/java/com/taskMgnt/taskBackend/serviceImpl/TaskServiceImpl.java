package com.taskMgnt.taskBackend.serviceImpl;

import com.taskMgnt.taskBackend.entity.Task;
import com.taskMgnt.taskBackend.entity.Users;
import com.taskMgnt.taskBackend.exception.APIException;
import com.taskMgnt.taskBackend.exception.TaskNotFound;
import com.taskMgnt.taskBackend.exception.UserNotFound;
import com.taskMgnt.taskBackend.payload.TaskDto;
import com.taskMgnt.taskBackend.repository.TaskRepository;
import com.taskMgnt.taskBackend.repository.UserRepository;
import com.taskMgnt.taskBackend.service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public TaskDto saveTask(long userid, TaskDto taskDto) {
        Users user = userRepository.findById(userid).orElseThrow(
                () -> new UserNotFound(String.format("User Id %d not found",userid))
        );
        Task task = modelMapper.map(taskDto, Task.class);
        task.setUsers(user);
        Task savedTask = taskRepository.save(task);
        return modelMapper.map(savedTask, TaskDto.class);
    }

    @Override
    public List<TaskDto> getAllTasks(long userid) {
        userRepository.findById(userid).orElseThrow(
                () -> new UserNotFound(String.format("User Id %d not found",userid))
        );
        List<Task> tasks = taskRepository.findAllByUsersId(userid);
        return tasks.stream().map(
                task -> modelMapper.map(task, TaskDto.class)
        ).collect(Collectors.toList());
    }

    @Override
    public TaskDto getTask(long userid, long taskid) {
        Users user = userRepository.findById(userid).orElseThrow(
                () -> new UserNotFound(String.format("User Id %d not found",userid))
        );
        Task task = taskRepository.findById(taskid).orElseThrow(
                () -> new TaskNotFound(String.format("Task with Id %d not found",taskid))
        );
        if(user.getId() != task.getUsers().getId()){
            throw new APIException(String.format("Task id %d is not belongs to User Id %d",taskid,userid));
        }
        return modelMapper.map(task, TaskDto.class);
    }

    @Override
    public void deleteTask(long userid, long taskid) {
        Users user = userRepository.findById(userid).orElseThrow(
                () -> new UserNotFound(String.format("User Id %d not found",userid))
        );
        Task task = taskRepository.findById(taskid).orElseThrow(
                () -> new TaskNotFound(String.format("Task with Id %d not found",taskid))
        );
        if(user.getId() != task.getUsers().getId()){
            throw new APIException(String.format("Task id %d is not belongs to User Id %d",taskid,userid));
        }
        taskRepository.deleteById(taskid);
    }
}
