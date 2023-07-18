package com.taskMgnt.taskBackend.serviceImpl;

import com.taskMgnt.taskBackend.entity.Users;
import com.taskMgnt.taskBackend.payload.UserDto;
import com.taskMgnt.taskBackend.repository.UserRepository;
import com.taskMgnt.taskBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDto createUser(UserDto userDto) {
        Users users = userDtoToEntity(userDto);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        Users savedUser = userRepository.save(users);
        return entityToUsersDto(savedUser);
    }

    private Users userDtoToEntity(UserDto userDto){
        Users users = new Users();
        users.setName(userDto.getName());
        users.setEmail(userDto.getEmail());
        users.setPassword(userDto.getPassword());

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
}
