package com.harsh.journalapp.JournalNest.dto;

import com.harsh.journalapp.JournalNest.enums.Mood;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnalyticsDTO {
    private int totalEntries;
    private int totalWordCount;
    private int avgWordsPerEntry;
    private int currentStreak;
    private int longestStreak;
    private Mood mostFrequentMood;
}
