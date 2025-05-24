package com.harsh.journalapp.JournalNest.repository;

import com.harsh.journalapp.JournalNest.entity.JournalEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalEntryRepository extends MongoRepository<JournalEntry,String> {
}
