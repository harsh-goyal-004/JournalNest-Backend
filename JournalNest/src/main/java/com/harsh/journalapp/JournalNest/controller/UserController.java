package com.harsh.journalapp.JournalNest.controller;

import com.harsh.journalapp.JournalNest.dto.UserLoginDTO;
import com.harsh.journalapp.JournalNest.dto.UserRegisterDTO;
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
    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UserRegisterDTO user)
    {
        try{
            String newUser = userService.createNewUser(user);
            return new ResponseEntity<>(newUser,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("username already exists", HttpStatus.CONFLICT);
        }
    }

//    Login User
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO userDTO, HttpServletResponse response){
         try{
             String accessToken = userService.login(userDTO, response);
             return new ResponseEntity<>(accessToken,HttpStatus.OK);
         } catch (Exception e) {
             return new ResponseEntity<>("Invalid username or password",HttpStatus.UNAUTHORIZED);
         }
    }

//    Get new access token
    @GetMapping("/get-access-token")
    public ResponseEntity<String> login(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        for(Cookie c : cookies){
            if("refreshToken".equals(c.getName())){
                String refreshToken = c.getValue();

                if(refreshToken != null){
                    try {
                        String newAccessToken = userService.generateNewAccessToken(refreshToken);
                        return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
                    } catch (Exception e) {
                        return new ResponseEntity<>("Refresh token is invalid or expired",HttpStatus.UNAUTHORIZED);
                    }
                }
            }
        }
        return new ResponseEntity<>("Refresh Token is invalid or expired", HttpStatus.UNAUTHORIZED);
    }

}
