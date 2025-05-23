package com.harsh.journalapp.JournalNest.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "user")
@Data
@NoArgsConstructor
public class User {

    @NonNull
    private String name;
    @NonNull
    @Id
    private String username;
    @NonNull
    private String password;
    private String email;
    private List<String> roles;
    @DBRef
    private List<JournalEntry> journalEntries =  new ArrayList<>();
}
