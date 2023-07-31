package com.taskMgnt.taskBackend.serviceImpl;

import com.taskMgnt.taskBackend.constants.TaskStatus;
import com.taskMgnt.taskBackend.entity.Task;
import com.taskMgnt.taskBackend.entity.Users;
import com.taskMgnt.taskBackend.exception.APIException;
import com.taskMgnt.taskBackend.exception.TaskNotFound;
import com.taskMgnt.taskBackend.exception.UserNotFound;
import com.taskMgnt.taskBackend.payload.TaskDto;
import com.taskMgnt.taskBackend.payload.CreateTaskDto;
import com.taskMgnt.taskBackend.payload.UpdateTaskDto;
import com.taskMgnt.taskBackend.repository.TaskRepository;
import com.taskMgnt.taskBackend.repository.UserRepository;
import com.taskMgnt.taskBackend.service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public TaskDto saveTask(CreateTaskDto taskDto) {
        String useremail = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findByEmail(useremail).orElseThrow(
                () -> new UserNotFound(String.format("User Id %d not found",useremail))
        );
        Task task = modelMapper.map(taskDto, Task.class);
        prePersistCreatedDate(task);
        prePersistUpdatedDate(task);
        prePersistStatus(task, "");
        prePersistIsDeleted(task,false);
        task.setUsers(user);
        Task savedTask = taskRepository.save(task);

        System.out.println();
        return modelMapper.map(savedTask, TaskDto.class);
    }

    @Override
    public List<TaskDto> getAllTasks() {
        String useremail = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findByEmail(useremail).orElseThrow(
                () -> new UserNotFound(String.format("User Id %d not found",useremail))
        );
        List<Task> tasks = taskRepository.findAllByUsersId(user.getId());
        return tasks.stream()
                .filter(t -> !t.isIsdeleted())
                .map(task -> modelMapper.map(task, TaskDto.class)
        ).collect(Collectors.toList());
    }

    @Override
    public TaskDto getTask(long taskid) {
        String useremail = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findByEmail(useremail).orElseThrow(
                () -> new UserNotFound(String.format("User Id %d not found",useremail))
        );
        Task task = taskRepository.findById(taskid).orElseThrow(
                () -> new TaskNotFound(String.format("Task with Id %d not found",taskid))
        );
        if(task.isIsdeleted()){
            throw new TaskNotFound(String.format("Task with Id %d not found",taskid));
        }
        if(user.getId() != task.getUsers().getId()){
            throw new APIException(String.format("Task id %d is not belongs to User Id %d",taskid,user.getId()));
        }
        return modelMapper.map(task, TaskDto.class);
    }

    @Override
    public TaskDto deleteTask(long taskid) {
        String useremail = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findByEmail(useremail).orElseThrow(
                () -> new UserNotFound(String.format("User Id %d not found",useremail))
        );
        Task task = taskRepository.findById(taskid).orElseThrow(
                () -> new TaskNotFound(String.format("Task with Id %d not found",taskid))
        );
        if(user.getId() != task.getUsers().getId()){
            throw new APIException(String.format("Task id %d is not belongs to User Id %d",taskid,user.getId()));
        }
        prePersistIsDeleted(task,true);
        Task savedTask = taskRepository.save(task);
        return modelMapper.map(savedTask, TaskDto.class);
//        taskRepository.deleteById(taskid);
    }

    @Override
    public TaskDto updateTask(long taskid, UpdateTaskDto updatedTaskDto) {
        String useremail = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findByEmail(useremail).orElseThrow(
                () -> new UserNotFound(String.format("User Id %d not found",useremail))
        );
        Task task = taskRepository.findById(taskid).orElseThrow(
                () -> new TaskNotFound(String.format("Task with Id %d not found",taskid))
        );
        task.setTaskname(updatedTaskDto.getTaskname());
        prePersistUpdatedDate(task);
        prePersistStatus(task,updatedTaskDto.getStatus());
        Task savedTask = taskRepository.save(task);
        return modelMapper.map(savedTask, TaskDto.class);
    }

    @Override
    public List<TaskDto> getAllUsersTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .filter(t -> !t.isIsdeleted())
                .map(task -> modelMapper.map(task, TaskDto.class)
                ).collect(Collectors.toList());
    }

    private void prePersistCreatedDate(Task task) {
        task.setCreatedat(LocalDateTime.now());
    }
    private void prePersistUpdatedDate(Task task) {
        task.setUpdatedat(LocalDateTime.now());
    }
    private void prePersistStatus(Task task, String status) {
        if(status.compareTo("Completed") == 0) {
            task.setStatus(String.valueOf(TaskStatus.Completed));
        } else if(status.compareTo("InProgress") == 0)
            task.setStatus(String.valueOf(TaskStatus.InProgress));
        else
            task.setStatus(String.valueOf(TaskStatus.Open));
    }
    private void prePersistIsDeleted(Task task, boolean isDeleted){
        task.setIsdeleted(isDeleted);
    }

}
