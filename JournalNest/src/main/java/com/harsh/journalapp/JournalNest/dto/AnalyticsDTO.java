package com.harsh.journalapp.JournalNest.dto;

import com.harsh.journalapp.JournalNest.enums.Mood;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
public class AnalyticsDTO {
    private int totalEntries;
    private Map<LocalDate,Integer> entriesPerDay;
    private int totalWordCount;
    private int avgWordsPerEntry;
    private Map<LocalDate,Integer> wordCountPerDay;
    private int currentStreak;
    private int longestStreak;
    private Map<Mood,Integer> moodDistribution;
    private Mood mostFrequentMood;
}
