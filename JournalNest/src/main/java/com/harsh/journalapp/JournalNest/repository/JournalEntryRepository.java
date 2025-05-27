package com.harsh.journalapp.JournalNest.repository;

import com.harsh.journalapp.JournalNest.entity.JournalEntry;
import com.harsh.journalapp.JournalNest.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JournalEntryRepository extends MongoRepository<JournalEntry,String> {
    Page<JournalEntry> findJournalEntriesByUser(User user, Pageable page);
    List<JournalEntry> findJournalEntriesByUser(User user);
}
