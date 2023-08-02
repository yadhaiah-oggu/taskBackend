package com.taskMgnt.taskBackend.serviceImpl;

import com.taskMgnt.taskBackend.entity.Users;
import com.taskMgnt.taskBackend.exception.UserAlreadyExisted;
import com.taskMgnt.taskBackend.exception.UserNotFound;
import com.taskMgnt.taskBackend.payload.RegisterUserDto;
import com.taskMgnt.taskBackend.payload.UserDto;
import com.taskMgnt.taskBackend.repository.UserRepository;
import com.taskMgnt.taskBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDto createUser(RegisterUserDto userDto) {
        if(userRepository.existsByEmail(userDto.getEmail())){
            throw new UserAlreadyExisted("User already Existed");
        }
        Users users = registerUserDtoToEntity(userDto);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setEmail(userDto.getEmail().toLowerCase());
        users.setName(userDto.getName().substring(0, 1).toUpperCase() + userDto.getName().substring(1));



        Users savedUser = userRepository.save(users);

        return entityToUsersDto(savedUser);
    }

    @Override
    public String getUsernameByEmail(String email) {

        if(!userRepository.existsByEmail(email)){
            throw new UserNotFound("User Not Found");
        }
        Users user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFound(String.format("User Id %d not found",email))
        );
        return user.getName();
    }

    @Override
    public String getUserRoleByEmail(String email) {
        Users user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFound(String.format("User Id %d not found",email))
        );
        Set<String> roles = user.getRoles();

        if(roles.contains("ROLE_ADMIN")){
            return "ADMIN";
        }
        else{
            return "USER";
        }
   }



    private Users userDtoToEntity(UserDto userDto){
        Users users = new Users();
        users.setName(userDto.getName());
        users.setEmail(userDto.getEmail());
        users.setPassword(userDto.getPassword());

        return users;
    }
    private Users registerUserDtoToEntity(RegisterUserDto userDto){
        Users users = new Users();
        users.setName(userDto.getName());
        users.setEmail(userDto.getEmail());
        users.setPassword(userDto.getPassword());

        Set<String> rolesWithPrefix = new HashSet<>();

        if(userDto.getRole().compareTo("ADMIN") == 0){
            rolesWithPrefix.add("ROLE_ADMIN");
            rolesWithPrefix.add("ROLE_USER");
        } else{
            rolesWithPrefix.add("ROLE_USER");
        }
        users.setRoles(rolesWithPrefix);

        return users;
    }
    private UserDto entityToUsersDto(Users users){
        UserDto userDto = new UserDto();
        userDto.setId(users.getId());
        userDto.setName(users.getName());
        userDto.setEmail(users.getEmail());
        userDto.setPassword(users.getPassword());

        return userDto;
    }
    private Users getUserByEmailId(String email){
        Users user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFound(String.format("User Id %d not found",email))
        );
       return user;
    }
}
