package com.harsh.journalapp.JournalNest.controller;

import com.harsh.journalapp.JournalNest.dto.JournalEntryDTO;
import com.harsh.journalapp.JournalNest.entity.JournalEntry;
import com.harsh.journalapp.JournalNest.mapper.JournalEntryMapper;
import com.harsh.journalapp.JournalNest.services.JournalEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            JournalEntry journalEntry = journalEntryService.createJournalEntry(journalEntryDTO, username);
            JournalEntryDTO journalEntryDTO1 = JournalEntryMapper.toDTO(journalEntry);
            return new ResponseEntity<>(journalEntryDTO1,HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>("Something went wrong while creating the journal entry", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    Get Single Journal Entry
    @GetMapping("/get-single-entry/{id}")
    public ResponseEntity<?> getSingleJournalEntry(@PathVariable  String id){
        JournalEntry journalEntry = journalEntryService.getJournalEntry(id);
        if(journalEntry != null){
            System.out.println(journalEntry);
           JournalEntryDTO journalEntryDTO = JournalEntryMapper.toDTO(journalEntry);
           return new ResponseEntity<>(journalEntryDTO,HttpStatus.OK);
       }
        return new ResponseEntity<>("No Journal Entry Found with the id: " + id, HttpStatus.NOT_FOUND);
    }

//    Get all Journal Entries
    @GetMapping("/get-all-entries")
    public ResponseEntity<?> getAllJournalEntries(@RequestParam(defaultValue = "1") int page){
        if(page < 1) page = 1;
        Page<JournalEntry> allJournalEntries = journalEntryService.getAllJournalEntries(page);
        if(allJournalEntries != null){
            Page<JournalEntryDTO> journalEntryDTO = JournalEntryMapper.toDTO(allJournalEntries);
            return new ResponseEntity<>(journalEntryDTO,HttpStatus.OK);
        }
        return new ResponseEntity<>("No Journal Entry Found", HttpStatus.NOT_FOUND);
    }

//    Update Single Journal Entry
    @PutMapping("/update-journal-entry/{id}")
    public ResponseEntity<?> updateJournalEntry(@RequestBody JournalEntryDTO journalEntryDTO, @PathVariable String id){
        try{
            String response = journalEntryService.updateJournalEntry(id, journalEntryDTO);
            if(response != null){
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>("No Journal Entry Found with the id: " + id, HttpStatus.NOT_FOUND);
        }catch(Exception e){
            return new ResponseEntity<>("Invalid Tags or Mood", HttpStatus.BAD_REQUEST);
        }
    }

//    Delete Single Journal
    @DeleteMapping("/delete-journal-entry/{id}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable String id){
        String response = journalEntryService.deleteJournalEntry(id);
        if(response != null){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>("No Journal Entry Found with id: " + id, HttpStatus.NOT_FOUND);
    }

//    Search and filter journal
    @GetMapping("/search-journal")
    public ResponseEntity<?> getFilterJournals(@RequestParam(required = false) String search, @RequestParam(required = false) String tag, @RequestParam(required = false) String mood){
        List<JournalEntry> journalEntries = journalEntryService.getFilterJournalEntries(search,tag,mood);
        if(journalEntries != null){
            List<JournalEntryDTO> journalEntryDTOS = JournalEntryMapper.toDTO(journalEntries);
            return new ResponseEntity<>(journalEntryDTOS,HttpStatus.OK);
        }
        return new ResponseEntity<>("No Journal Entry Found", HttpStatus.NOT_FOUND);
    }


}
