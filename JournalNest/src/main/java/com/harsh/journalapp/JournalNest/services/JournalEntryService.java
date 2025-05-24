package com.harsh.journalapp.JournalNest.services;

import com.harsh.journalapp.JournalNest.dto.JournalEntryDTO;
import com.harsh.journalapp.JournalNest.entity.JournalEntry;
import com.harsh.journalapp.JournalNest.entity.User;
import com.harsh.journalapp.JournalNest.mapper.JournalEntryMapper;
import com.harsh.journalapp.JournalNest.repository.JournalEntryRepository;
import com.harsh.journalapp.JournalNest.repository.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void createJournalEntry(JournalEntryDTO journalEntryDTO, String username){
        User user = userRespository.findByUsername(username);
        JournalEntry journalEntry = JournalEntryMapper.toEntity(journalEntryDTO);
        journalEntry.setId(UUID.randomUUID().toString());
        journalEntry.setCreatedAt(LocalDateTime.now());

        JournalEntry journalEntry1 = journalEntryRepository.save(journalEntry);

        user.getJournalEntries().add(journalEntry1);

        userService.saveUserWithJournalRef(user);
    }


//    GET Single Journal Entry
    public ResponseEntity<?> getJournalEntry(String journalEntryId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRespository.findByUsername(username);

        if(user != null){
            List<JournalEntry> journalEntries = user.getJournalEntries();

            if(!journalEntries.isEmpty()){
                for(JournalEntry journalEntry : journalEntries){
                    if(journalEntry.getId().equals(journalEntryId)){
                        return new ResponseEntity<>(journalEntry,HttpStatus.OK);
                    }
                }
            }
        }
        return new ResponseEntity<>("No Journal Entry Found", HttpStatus.NOT_FOUND);
    }


//    GET All Journal Entries
    public ResponseEntity<List<JournalEntry>> getAllJournalEntries(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRespository.findByUsername(username);

        if(user != null){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            if(!journalEntries.isEmpty()){
                return new ResponseEntity<>(journalEntries,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


//    Update Single Journal Entry
    public ResponseEntity<String> updateJournalEntry(String journalEntryId, JournalEntryDTO journalEntryDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRespository.findByUsername(username);

        List<JournalEntry> journalEntries = user.getJournalEntries();

        if (!journalEntries.isEmpty()) {
            for (JournalEntry journalEntry : journalEntries) {
                if (journalEntry.getId().equals(journalEntryId)) {
                    Optional<JournalEntry> existingJournalEntry = journalEntryRepository.findById(journalEntryId);

                    if (existingJournalEntry.isPresent()) {
                        JournalEntry updatedJournalEntry = existingJournalEntry.get();
                        updatedJournalEntry.setTitle(journalEntryDTO.getTitle());
                        updatedJournalEntry.setContent(journalEntryDTO.getContent());
                        updatedJournalEntry.setMood(journalEntryDTO.getMood());
                        updatedJournalEntry.setTags(journalEntryDTO.getTags());

                        journalEntryRepository.save(updatedJournalEntry);
                        return new ResponseEntity<>("Journal Entry Updated Successfully", HttpStatus.OK);
                    }
                }
            }
        }

        return new ResponseEntity<>("No Journal Entry Found", HttpStatus.NOT_FOUND);
    }


//    Delete Single Journal Entry
    public ResponseEntity<?> deleteJournalEntry(String journalEntryId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRespository.findByUsername(username);

        List<JournalEntry> journalEntries = user.getJournalEntries();

        if(!journalEntries.isEmpty()){
            for(JournalEntry journalEntry : journalEntries){
                if(journalEntry.getId().equals(journalEntryId)){
                    journalEntryRepository.deleteById(journalEntryId);
                    journalEntries.removeIf(journal -> journal.getId().equals(journalEntryId));
                    user.setJournalEntries(journalEntries);
                    userService.saveUserWithJournalRef(user);
                    return new ResponseEntity<>("Journal Entry Deleted Successfully",HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("Journal Entry not found", HttpStatus.NOT_FOUND);
    }

}
