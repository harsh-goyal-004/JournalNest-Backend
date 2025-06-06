package com.harsh.journalapp.JournalNest.mapper;

import com.harsh.journalapp.JournalNest.dto.JournalEntryDTO;
import com.harsh.journalapp.JournalNest.entity.JournalEntry;
import com.harsh.journalapp.JournalNest.enums.Mood;
import com.harsh.journalapp.JournalNest.enums.Tags;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JournalEntryMapper {

    public static List<Tags> stringToEnum(List<String> tags){
        List<Tags> enumTags = new ArrayList<>();
        for(String tag : tags){
            Tags enumTag = Tags.valueOf(tag.toUpperCase());
            enumTags.add(enumTag);
        }
        return enumTags;
    }

    public static List<String> enumToString(List<Tags> tags){
        List<String> stringTags = new ArrayList<>();
        for(Tags tag : tags){
            stringTags.add(tag.name());
        }
        return stringTags;
    }

    public static JournalEntry toEntity(JournalEntryDTO journalEntryDTO){
        try{
            JournalEntry journalEntry = new JournalEntry();
            journalEntry.setTitle(journalEntryDTO.getTitle());
            journalEntry.setContent(journalEntryDTO.getContent());
            journalEntry.setMood(Mood.valueOf(journalEntryDTO.getMood().toUpperCase()));
            journalEntry.setTags(stringToEnum(journalEntryDTO.getTags()));
            journalEntry.setStarred(journalEntryDTO.isStarred());
            return journalEntry;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid tag or mood");
        }
    }

    public static JournalEntryDTO toDTO(JournalEntry journalEntry){
        try{
            JournalEntryDTO journalEntryDTO = new JournalEntryDTO();
            journalEntryDTO.setId(journalEntry.getId());
            journalEntryDTO.setTitle(journalEntry.getTitle());
            journalEntryDTO.setContent(journalEntry.getContent());
            journalEntryDTO.setTags(enumToString(journalEntry.getTags()));
            journalEntryDTO.setMood(journalEntry.getMood().name());
            journalEntryDTO.setCreatedAt(journalEntry.getCreatedAt());
            journalEntryDTO.setStarred(journalEntry.isStarred());
            return journalEntryDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Page<JournalEntryDTO> toDTO(Page<JournalEntry> journalEntries){
        return journalEntries.map(JournalEntryMapper::toDTO);
    }

    public static List<JournalEntryDTO> toDTO(List<JournalEntry> journalEntries){
        return journalEntries.stream().map(JournalEntryMapper::toDTO).collect(Collectors.toList());
    }
}
