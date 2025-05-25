package com.harsh.journalapp.JournalNest.services;

import com.harsh.journalapp.JournalNest.dto.JournalEntryDTO;
import com.harsh.journalapp.JournalNest.entity.JournalEntry;
import com.harsh.journalapp.JournalNest.entity.User;
import com.harsh.journalapp.JournalNest.enums.Mood;
import com.harsh.journalapp.JournalNest.mapper.JournalEntryMapper;
import com.harsh.journalapp.JournalNest.repository.JournalEntryRepository;
import com.harsh.journalapp.JournalNest.repository.UserRespository;
import com.harsh.journalapp.JournalNest.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JournalEntryService {

    @Autowired
    private UserRespository userRespository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

//    Create a new Journal Entry
    @Transactional
    public ResponseEntity<JournalEntry> createJournalEntry(JournalEntryDTO journalEntryDTO, String username){
        User user = userRespository.findByUsername(username);
        JournalEntry journalEntry = JournalEntryMapper.toEntity(journalEntryDTO);

        // Generate a random id for each journal entry and get current time
        journalEntry.setId(UUID.randomUUID().toString());
        journalEntry.setCreatedAt(LocalDateTime.now());

        // Count the number of words
        journalEntry.setWordCount(TextUtils.calculateWordCount(journalEntryDTO.getContent()));

        // Add User Reference
        journalEntry.setUser(user);

        JournalEntry journalEntry1 = journalEntryRepository.save(journalEntry);

        return new ResponseEntity<>(journalEntry1, HttpStatus.OK);
    }


//    GET Single Journal Entry
    public ResponseEntity<?> getJournalEntry(String journalEntryId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<JournalEntry> journalEntry = journalEntryRepository.findById(journalEntryId);

        if(journalEntry.isPresent()){
            JournalEntry journalEntry1 = journalEntry.get();
            User user = journalEntry1.getUser();
            if(user != null){
               if(user.getUsername().equals(username)){
                   return new ResponseEntity<>(journalEntry1, HttpStatus.OK);
               }
            }
        }

        return new ResponseEntity<>("No Journal Entry Found", HttpStatus.NOT_FOUND);
    }


//    GET All Journal Entries
    public ResponseEntity<Page<JournalEntry>> getAllJournalEntries(int page){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRespository.findByUsername(username);

        if(user != null){
            Pageable pageable = PageRequest.of(page - 1 ,10, Sort.by("createdAt").descending());
            Page<JournalEntry> journalEntries = journalEntryRepository.findJournalEntriesByUser(user,pageable);
            return new ResponseEntity<>(journalEntries,HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


//    Update Single Journal Entry
    public ResponseEntity<String> updateJournalEntry(String journalEntryId, JournalEntryDTO journalEntryDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Optional<JournalEntry> existingJournalEntry = journalEntryRepository.findById(journalEntryId);

            if (existingJournalEntry.isPresent()) {
                JournalEntry updatedJournalEntry = existingJournalEntry.get();

                User user = updatedJournalEntry.getUser();

                if (user != null && username != null) {
                    if (user.getUsername().equals(username)) {
                        updatedJournalEntry.setTitle(journalEntryDTO.getTitle());
                        updatedJournalEntry.setContent(journalEntryDTO.getContent());
                        updatedJournalEntry.setMood(Mood.valueOf(journalEntryDTO.getMood().toUpperCase()));
                        updatedJournalEntry.setTags(JournalEntryMapper.stringToEnum(journalEntryDTO.getTags()));

                        // Count the number of words
                        updatedJournalEntry.setWordCount(TextUtils.calculateWordCount(journalEntryDTO.getContent()));

                        journalEntryRepository.save(updatedJournalEntry);
                        return new ResponseEntity<>("Journal Entry Updated Successfully", HttpStatus.OK);

                    }
                }
            }

            return new ResponseEntity<>("No Journal Entry Found", HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid mood or tags");
        }
    }



//    Delete Single Journal Entry
    public ResponseEntity<?> deleteJournalEntry(String journalEntryId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<JournalEntry> journalEntry = journalEntryRepository.findById(journalEntryId);

        if(journalEntry.isPresent()){
            User user = journalEntry.get().getUser();

            if(user != null && username != null){
                if(user.getUsername().equals(username)){
                    journalEntryRepository.deleteById(journalEntryId);
                    return new ResponseEntity<>("Journal Entry Deleted Successfully", HttpStatus.NO_CONTENT);
                }
            }
        }
        return new ResponseEntity<>("Journal Entry not found", HttpStatus.NOT_FOUND);
    }

}
