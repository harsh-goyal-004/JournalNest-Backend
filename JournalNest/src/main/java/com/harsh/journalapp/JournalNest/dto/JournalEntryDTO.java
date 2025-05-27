package com.harsh.journalapp.JournalNest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import java.util.List;

@Data
@NoArgsConstructor
public class JournalEntryDTO {
    private String id;
    @NonNull
    private String title;
    @NonNull
    private String content;
    @NonNull
    private List<String> tags;
    @NonNull
    private String mood;
}
