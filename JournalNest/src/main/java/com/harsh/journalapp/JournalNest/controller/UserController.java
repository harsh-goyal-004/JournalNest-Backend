package com.harsh.journalapp.JournalNest.controller;

import com.harsh.journalapp.JournalNest.dto.UserDTO;
import com.harsh.journalapp.JournalNest.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

//    Create New User
    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody UserDTO user)
    {
        return userService.createNewUser(user);
    }

//    Login User
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO, HttpServletResponse response){
        return userService.login(userDTO,response);
    }

//    Get new access token
    @GetMapping("/get-access-token")
    public ResponseEntity<String> login(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        for(Cookie c : cookies){
            if("refreshToken".equals(c.getName())){
                String refreshToken = c.getValue();

                if(refreshToken != null){
                    return userService.generateNewAccessToken(refreshToken);
                }
            }
        }
        return new ResponseEntity<>("Refresh Token is invalid or expired", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/hello")
    public String sayHello(){
        return "Hello";
    }

}
