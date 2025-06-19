package com.harsh.journalapp.JournalNest.controller;

import com.harsh.journalapp.JournalNest.dto.UserInfoDTO;
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
import org.springframework.web.multipart.MultipartFile;

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
    @GetMapping("/refresh-token")
    public ResponseEntity<String> login(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        if(cookies != null){
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
        }
        return new ResponseEntity<>("Refresh Token is invalid or expired", HttpStatus.UNAUTHORIZED);
    }

//    Get User Information
    @GetMapping("/get-user-info")
    public ResponseEntity<?> getUserInfo(){
        UserInfoDTO userInfo = userService.getUserInfo();
        if(userInfo != null){
            return new ResponseEntity<>(userInfo, HttpStatus.OK);
        }
        return new ResponseEntity<>("User Not Found!", HttpStatus.NOT_FOUND);
    }

//    Update User Information
    @PutMapping("/update-user-info")
    public ResponseEntity<String> updateUserInfo(@RequestBody UserInfoDTO userInfoDTO){
        try{
            String response = userService.updateUserInfo(userInfoDTO);
            if(response != null){
                return new ResponseEntity<>(response,HttpStatus.OK);
            }
            return new ResponseEntity<>("User Not Found!", HttpStatus.NOT_FOUND);
        }catch(Exception e){
            return new ResponseEntity<>("Something went wrong while updating the user information!",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

//    Upload Profile Picture
    @PutMapping("/upload-profile-pic")
    public ResponseEntity<String> uploadProfilePic(@RequestBody MultipartFile profilePic){
        try {
            String res = userService.uploadProfilePic(profilePic);
            if(res != null){
                return new ResponseEntity<>(res,HttpStatus.OK);
            }
            return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>("Something went wrong while updating the user profile picture",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
