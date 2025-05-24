package com.harsh.journalapp.JournalNest.controller;

import com.harsh.journalapp.JournalNest.dto.JournalEntryDTO;
import com.harsh.journalapp.JournalNest.services.JournalEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

//    Create New Journal
    @PostMapping("/create-journal-entry")
    public ResponseEntity<?> createNewJournalEntry(@RequestBody JournalEntryDTO journalEntryDTO){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            journalEntryService.createJournalEntry(journalEntryDTO,username);
            return new ResponseEntity<>("Journal Entry created Successfully", HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("Something went wrong while creating the journal entry", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    Get Single Journal Entry
    @GetMapping("/get-single-entry/{id}")
    public ResponseEntity<?> getSingleJournalEntry(@PathVariable  String id){
        return journalEntryService.getJournalEntry(id);
    }

//    Get all Journal Entries
    @GetMapping("/get-all-entries")
    public ResponseEntity<?> getAllJournalEntries(){
        return journalEntryService.getAllJournalEntries();
    }

//    Update Single Journal Entry
    @PutMapping("/update-journal-entry/{id}")
    public ResponseEntity<?> updateJournalEntry(@RequestBody JournalEntryDTO journalEntryDTO, @PathVariable String id){
        return journalEntryService.updateJournalEntry(id,journalEntryDTO);
    }

//    Delete Single Journal
    @DeleteMapping("/delete-journal-entry/{id}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable String id){
        return journalEntryService.deleteJournalEntry(id);
    }


}
