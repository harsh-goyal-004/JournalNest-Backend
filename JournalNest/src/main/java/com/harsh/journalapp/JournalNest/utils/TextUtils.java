package com.harsh.journalapp.JournalNest.utils;

public class TextUtils {

//    This method counts the total words in a journal entry content
    public static int calculateWordCount(String journalContent){
        int wordCount = journalContent.trim().split("\\s+").length;
        return wordCount;
    }
}
