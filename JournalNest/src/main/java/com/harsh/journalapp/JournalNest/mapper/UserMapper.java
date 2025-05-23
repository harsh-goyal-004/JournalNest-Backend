package com.harsh.journalapp.JournalNest.mapper;

import com.harsh.journalapp.JournalNest.dto.UserDTO;
import com.harsh.journalapp.JournalNest.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

//    This is a mapper method that maps the UserDTO values to User
    public static User toEntity(UserDTO userDTO){
        User user = new User();
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        if(userDTO.getEmail() != null){
            user.setEmail(userDTO.getEmail());
        }

        return user;
    }
}
