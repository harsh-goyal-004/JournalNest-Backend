package com.harsh.journalapp.JournalNest.services;

import com.harsh.journalapp.JournalNest.dto.AnalyticsDTO;
import com.harsh.journalapp.JournalNest.entity.JournalEntry;
import com.harsh.journalapp.JournalNest.entity.User;
import com.harsh.journalapp.JournalNest.repository.JournalEntryRepository;
import com.harsh.journalapp.JournalNest.repository.UserRespository;
import com.harsh.journalapp.JournalNest.utils.AnalyticsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserRespository userRespository;


    public AnalyticsDTO getAnalyticsSummary(){
        AnalyticsDTO analyticsDTO = new AnalyticsDTO();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(username != null && !username.isEmpty()){
//            Get the user from DB
            User user = userRespository.findByUsername(username);

//            Get all the journalEntries of the user
            List<JournalEntry> journalEntries = journalEntryRepository.findJournalEntriesByUser(user);
            analyticsDTO.setTotalEntries(journalEntries.size()); // Total Entries

//            Total Entries Per Day
            analyticsDTO.setEntriesPerDay(AnalyticsUtils.getEntriesPerDay(journalEntries));

            int totalWordCount = AnalyticsUtils.getTotalWordCount(journalEntries);
            analyticsDTO.setTotalWordCount(totalWordCount); // Total wordCount of all entries

            analyticsDTO.setAvgWordsPerEntry(AnalyticsUtils.getAverageWordCount(journalEntries, totalWordCount )); // Average wordCount of each entry

            int currentStreak = AnalyticsUtils.getCurrentStreak(journalEntries);
            analyticsDTO.setCurrentStreak(currentStreak); // Current Streak of user

            int longestStreak = AnalyticsUtils.getLongestStreak(currentStreak, user.getLongestStreak()); // Get the new longest streak of the user
            user.setLongestStreak(longestStreak); // Set the new longest streak
            userRespository.save(user); // Save the user with the newly generated longest streak
            analyticsDTO.setLongestStreak(longestStreak); // Set the longest streak in analytics summary
            analyticsDTO.setWordCountPerDay(AnalyticsUtils.getWordCountPerDay(journalEntries));
            analyticsDTO.setMoodDistribution(AnalyticsUtils.getMoodDistribution(journalEntries));
            analyticsDTO.setMostFrequentMood(AnalyticsUtils.getMostFrequentMood(journalEntries)); // Most frequent Mood of the user

            return analyticsDTO;
        }
        return null;
    }
}
