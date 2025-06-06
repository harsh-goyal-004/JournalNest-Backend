package com.harsh.journalapp.JournalNest.utils;

import com.harsh.journalapp.JournalNest.entity.JournalEntry;
import com.harsh.journalapp.JournalNest.entity.User;
import com.harsh.journalapp.JournalNest.enums.Mood;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.*;

public class AnalyticsUtils {

//    Get Total Entries Per Day
    public static Map<LocalDate,Integer> getEntriesPerDay(List<JournalEntry> journalEntries){
        Map<LocalDate, Integer> getTotalEntriesPerDay = new HashMap<>();

        for(JournalEntry journalEntry : journalEntries){
            LocalDate date = journalEntry.getCreatedAt().toLocalDate();

            if(getTotalEntriesPerDay.containsKey(date)){
                getTotalEntriesPerDay.put(date, getTotalEntriesPerDay.get(date) + 1);
            }else{
                getTotalEntriesPerDay.put(date,1);
            }
        }

        return getTotalEntriesPerDay;
    }


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

//    The number of times a mood appeared in an entry
    public static Map<Mood,Integer> getMoodDistribution(List<JournalEntry> journalEntries){
        Map<Mood, Integer> moodDistribution = new HashMap<>();

        for(JournalEntry journalEntry : journalEntries){
            if(moodDistribution.containsKey(journalEntry.getMood())){
                moodDistribution.put(journalEntry.getMood(), moodDistribution.get(journalEntry.getMood()) + 1);
            }else{
                moodDistribution.put(journalEntry.getMood(), 1);
            }
        }
        return moodDistribution;
    }

//    Most frequently appeared mood
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

//    This method calculates the word count of all the entries made in a day
    public static Map<LocalDate,Integer > getWordCountPerDay(List<JournalEntry> journalEntries){
        Map<LocalDate,Integer> wordCount = new HashMap<>();
        for(JournalEntry journalEntry : journalEntries){
            LocalDate date = journalEntry.getCreatedAt().toLocalDate();
            if(wordCount.containsKey(date)){
                wordCount.put(date, wordCount.get(date) + journalEntry.getWordCount());
            }else{
                wordCount.put(date,journalEntry.getWordCount());
            }
        }
        return wordCount;
    }

}
