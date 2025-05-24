package com.harsh.journalapp.JournalNest.dto;

import lombok.Data;
import lombok.NonNull;
import java.util.List;

@Data
public class JournalEntryDTO {
    @NonNull
    private String title;
    @NonNull
    private String content;
    @NonNull
    private List<String> tags;
    @NonNull
    private String mood;
}
