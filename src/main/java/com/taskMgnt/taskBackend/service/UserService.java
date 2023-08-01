package com.taskMgnt.taskBackend.service;

import com.taskMgnt.taskBackend.payload.RegisterUserDto;
import com.taskMgnt.taskBackend.payload.UserDto;

public interface UserService {
    public UserDto createUser(RegisterUserDto userDto);
    public String getUsernameByEmail(String email);
    public String getUserRoleByEmail(String email);
}
