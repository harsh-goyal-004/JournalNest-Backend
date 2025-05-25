package com.harsh.journalapp.JournalNest.mapper;

import com.harsh.journalapp.JournalNest.dto.JournalEntryDTO;
import com.harsh.journalapp.JournalNest.entity.JournalEntry;
import com.harsh.journalapp.JournalNest.enums.Mood;
import com.harsh.journalapp.JournalNest.enums.Tags;

import java.util.ArrayList;
import java.util.List;

public class JournalEntryMapper {

    public static List<Tags> stringToEnum(List<String> tags){
        List<Tags> enumTags = new ArrayList<>();
        for(String tag : tags){
            Tags enumTag = Tags.valueOf(tag.toUpperCase());
            enumTags.add(enumTag);
        }
        return enumTags;
    }

    public static JournalEntry toEntity(JournalEntryDTO journalEntryDTO){
        try{
            JournalEntry journalEntry = new JournalEntry();
            journalEntry.setTitle(journalEntryDTO.getTitle());
            journalEntry.setContent(journalEntryDTO.getContent());
            journalEntry.setMood(Mood.valueOf(journalEntryDTO.getMood().toUpperCase()));
            journalEntry.setTags(stringToEnum(journalEntryDTO.getTags()));
            return journalEntry;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid tag or mood");
        }
    }
}
