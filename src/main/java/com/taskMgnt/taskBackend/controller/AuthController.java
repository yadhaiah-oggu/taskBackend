package com.taskMgnt.taskBackend.controller;

import com.taskMgnt.taskBackend.entity.Users;
import com.taskMgnt.taskBackend.exception.UserAlreadyExisted;
import com.taskMgnt.taskBackend.payload.JWTAuthResponse;
import com.taskMgnt.taskBackend.payload.LoginDto;
import com.taskMgnt.taskBackend.payload.RegisterUserDto;
import com.taskMgnt.taskBackend.payload.UserDto;
import com.taskMgnt.taskBackend.security.JwtTokenProvider;
import com.taskMgnt.taskBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    //POST store User Data in DB
    @PostMapping("/register")
    public ResponseEntity<Object> createUser(@RequestBody RegisterUserDto userDto) {
        try {
            UserDto savedUser = userService.createUser(userDto);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        }
        catch (UserAlreadyExisted e){
            String alreadyExisted = "Email already Existed";
            return new ResponseEntity<>(alreadyExisted,HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.OK);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> loginUser(@RequestBody LoginDto loginDto){
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
                );
        System.out.println(authentication);
        //get token
        String token = jwtTokenProvider.generateToken(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(new JWTAuthResponse(token,userService.getUsernameByEmail(authentication.getName()), userService.getUserRoleByEmail(authentication.getName())));
    }
}