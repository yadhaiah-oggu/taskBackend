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
        try {
            String useremail = SecurityContextHolder.getContext().getAuthentication().getName();

            Users user = userRepository.findByEmail(useremail).orElseThrow(
                    () -> new UserNotFound(String.format("User Id %d not found", useremail))
            );
            Task task = modelMapper.map(taskDto, Task.class);
            prePersistCreatedDate(task);
            prePersistUpdatedDate(task);
            prePersistStatus(task, "");
            prePersistIsDeleted(task, false);
            task.setUsers(user);
            Task savedTask = taskRepository.save(task);


            TaskDto taskDtoResult = modelMapper.map(savedTask, TaskDto.class);

            taskDtoResult.setUsername(savedTask.getUsers().getName());

            return taskDtoResult;
        }
        catch (Exception e){
            throw new APIException("Something Went wrong with Database Connection");
        }
    }

    @Override
    public List<TaskDto> getAllTasks() {
        try{
            String useremail = SecurityContextHolder.getContext().getAuthentication().getName();
            Users user = userRepository.findByEmail(useremail).orElseThrow(
                    () -> new UserNotFound(String.format("User Id %d not found",useremail))
            );
            List<Task> tasks = taskRepository.findAllByUsersId(user.getId());
            return tasks.stream()
                    .filter(t -> !t.isIsdeleted())
                    .map(task -> {
                        TaskDto taskDto = modelMapper.map(task, TaskDto.class);

                        taskDto.setUsername(task.getUsers().getName());

                        return taskDto;
                    })
                    .collect(Collectors.toList());
        }
        catch (Exception e){
            throw new APIException("Something Went wrong with Database Connection");
        }
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
        if(!user.getRoles().contains("ROLE_ADMIN")){
            if(user.getId() != task.getUsers().getId() ) {
                throw new APIException(String.format("Task id %d is not belongs to User Id %d",taskid,user.getId()));
            }
        }
        TaskDto taskDto = modelMapper.map(task, TaskDto.class);

        taskDto.setUsername(task.getUsers().getName());
        return taskDto;
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
        if(task.isIsdeleted()){
            throw  new TaskNotFound(String.format("Task with Id %d not found",taskid));
        }
        if(!user.getRoles().contains("ROLE_ADMIN")){
            if(user.getId() != task.getUsers().getId() ) {
                throw new APIException(String.format("Task id %d is not belongs to User Id %d",taskid,user.getId()));
            }
        }



        prePersistIsDeleted(task,true);
        Task savedTask = taskRepository.save(task);
        TaskDto taskDto = modelMapper.map(savedTask, TaskDto.class);

        taskDto.setUsername(savedTask.getUsers().getName());
        return taskDto;
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
        if(task.isIsdeleted()){
            throw  new TaskNotFound(String.format("Task with Id %d not found",taskid));
        }

        if(!user.getRoles().contains("ROLE_ADMIN")) {
            if (user.getId() != task.getUsers().getId()) {
                throw new APIException(String.format("Task id %d is not belongs to User Id %d", taskid, user.getId()));
            }
        }

        task.setTaskname(updatedTaskDto.getTaskname());
        prePersistUpdatedDate(task);
        prePersistStatus(task,updatedTaskDto.getStatus());
        Task savedTask = taskRepository.save(task);

        TaskDto taskDto = modelMapper.map(savedTask, TaskDto.class);

        taskDto.setUsername(savedTask.getUsers().getName());
        return taskDto;
    }

    @Override
    public List<TaskDto> getAllUsersTasks() {
        try{
            List<Task> tasks = taskRepository.findAll();
//            return tasks.stream()
//                    .filter(t -> !t.isIsdeleted())
//                    .map(task ->
//                            modelMapper.map(task, TaskDto.class)
//                    ).collect(Collectors.toList());

            return tasks.stream()
                    .filter(t -> !t.isIsdeleted())
                    .map(task -> {
                        TaskDto taskDto = modelMapper.map(task, TaskDto.class);

                        taskDto.setUsername(task.getUsers().getName());

                        return taskDto;
                    })
                    .collect(Collectors.toList());


        }
        catch (Exception e){
            throw new APIException("Something Went wrong with Database Connection");
        }
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
