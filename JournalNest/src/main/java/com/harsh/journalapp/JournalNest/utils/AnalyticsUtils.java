package com.harsh.journalapp.JournalNest.utils;

import com.harsh.journalapp.JournalNest.entity.JournalEntry;
import com.harsh.journalapp.JournalNest.entity.User;
import com.harsh.journalapp.JournalNest.enums.Mood;

import java.time.LocalDate;
import java.util.*;

public class AnalyticsUtils {

    public static int getTotalWordCount(List<JournalEntry> journalEntries){
        int total = 0;
        for(JournalEntry journalEntry : journalEntries){
            total += journalEntry.getWordCount();
        }
        return total;
    }

    public static int getAverageWordCount(List<JournalEntry> journalEntries, int totalWordCount){
        int avg = totalWordCount/journalEntries.size();
        return avg;
    }

    public static Mood getMostFrequentMood(List<JournalEntry> journalEntries){
        Map<Mood, Integer> frequentMood = new HashMap<>();
        int max = 0;
        Mood mood = null;

        for(JournalEntry journalEntry : journalEntries){
            if(frequentMood.containsKey(journalEntry.getMood())){
                frequentMood.put(journalEntry.getMood(), frequentMood.get(journalEntry.getMood()) + 1);
            }else{
                frequentMood.put(journalEntry.getMood(), 1);
            }
        }

        for(Mood key : frequentMood.keySet()){
            if(max < frequentMood.get(key)){
                max = frequentMood.get(key);
                mood = key;
            }
        }

        return mood;
    }

    public static int getCurrentStreak(List<JournalEntry> journalEntries){
        int currentStreak = 0;
        Set<LocalDate> journalEntryDateSet = new HashSet<>();

//        Add all Journal Entry date in Set to remove duplicates
        for(JournalEntry journalEntry : journalEntries){
            LocalDate date = journalEntry.getCreatedAt().toLocalDate();
            journalEntryDateSet.add(date);
        }

//        Convert the set in a list for sorting
        List<LocalDate> journalEntryDateList = new ArrayList<>(journalEntryDateSet);

//        Sort the dates in descending order
        journalEntryDateList.sort(Comparator.reverseOrder());


//        Check if user has added a journal entry today or yesterday
        if(LocalDate.now().isEqual(journalEntryDateList.get(0)) || LocalDate.now().minusDays(1).isEqual(journalEntryDateList.get(0))) {

            currentStreak = 1; // Add the today or yesterday in currentStreak

//            Loop through the list and count the current streak
            for (int i = 0; i < journalEntryDateList.size() - 1; i++) {
                LocalDate currDate = journalEntryDateList.get(i);
                LocalDate prevDate = journalEntryDateList.get(i + 1);

                if (currDate.minusDays(1).isEqual(prevDate)) {
                    currentStreak += 1;
                }else{
                    break;
                }
            }
        }
        return currentStreak;
    }


    public static int getLongestStreak(int currentStreak, int longestStreak){
        if(currentStreak > longestStreak){
            longestStreak = currentStreak;
            return longestStreak;
        }else{
            return longestStreak;
        }
    }

}
