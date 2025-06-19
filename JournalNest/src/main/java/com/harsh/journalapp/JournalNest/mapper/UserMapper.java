package com.harsh.journalapp.JournalNest.mapper;

import com.harsh.journalapp.JournalNest.dto.UserInfoDTO;
import com.harsh.journalapp.JournalNest.dto.UserLoginDTO;
import com.harsh.journalapp.JournalNest.dto.UserRegisterDTO;
import com.harsh.journalapp.JournalNest.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

//    This is a mapper method that maps the UserRegisterDTO values to User
    public static User toEntity(UserRegisterDTO userDTO){
        User user = new User();
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        if(userDTO.getEmail() != null){
            user.setEmail(userDTO.getEmail());
        }

        return user;
    }

    //    This is a mapper method that maps the UserLoginDTO values to User
    public static User toEntity(UserLoginDTO userDTO){
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        return user;
    }

//    This is a mapper method that maps user to userInfoDTO
    public static UserInfoDTO toDTO(User user){
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setName(user.getName());
        userInfoDTO.setUsername(user.getUsername());
        userInfoDTO.setEmail(user.getEmail());
        userInfoDTO.setGender(user.getGender());
        userInfoDTO.setNumber(user.getNumber());
        userInfoDTO.setDateOfBirth(user.getDateOfBirth());
        userInfoDTO.setProfileImageUrl(user.getProfileUrl());
        return userInfoDTO;
    }
}
