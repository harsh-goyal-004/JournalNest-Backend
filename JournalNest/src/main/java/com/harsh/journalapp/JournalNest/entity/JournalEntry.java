package com.harsh.journalapp.JournalNest.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.harsh.journalapp.JournalNest.enums.Mood;
import com.harsh.journalapp.JournalNest.enums.Tags;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "journal_entry")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntry {

    @Id
    private String id;

    @NonNull
    private String title;

    @NonNull
    private String content;

    @NonNull
    private List<Tags> tags;

    @NonNull
    private Mood mood;

    private int wordCount;

    @DBRef
    private User user;

    @JsonFormat(pattern = "dd-MM-yy HH:mm:ss")
    private LocalDateTime createdAt;
}
