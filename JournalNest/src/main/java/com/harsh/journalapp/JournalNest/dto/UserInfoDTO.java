package com.harsh.journalapp.JournalNest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
    @NonNull
    private String name;
    private String username;
    private String email;
    private String gender;
    private int number;
    private LocalDate dateOfBirth;
    private String profileImageUrl;
}
