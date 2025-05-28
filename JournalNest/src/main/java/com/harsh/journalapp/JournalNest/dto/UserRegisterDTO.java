package com.harsh.journalapp.JournalNest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class UserRegisterDTO {
    @NonNull
    private String name;
    @NonNull
    private  String username;
    @NonNull
    private String password;
    private String email;
}
