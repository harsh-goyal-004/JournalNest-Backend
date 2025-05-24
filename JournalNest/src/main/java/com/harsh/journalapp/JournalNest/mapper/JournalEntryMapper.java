package com.harsh.journalapp.JournalNest.mapper;

import com.harsh.journalapp.JournalNest.dto.JournalEntryDTO;
import com.harsh.journalapp.JournalNest.entity.JournalEntry;

public class JournalEntryMapper {

    public static JournalEntry toEntity(JournalEntryDTO journalEntryDTO){
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setTitle(journalEntryDTO.getTitle());
        journalEntry.setContent(journalEntryDTO.getContent());
        journalEntry.setMood(journalEntryDTO.getMood());
        journalEntry.setTags(journalEntryDTO.getTags());

        return journalEntry;

    }
}
