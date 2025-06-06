package com.harsh.journalapp.JournalNest.services;

import com.harsh.journalapp.JournalNest.dto.JournalEntryDTO;
import com.harsh.journalapp.JournalNest.entity.JournalEntry;
import com.harsh.journalapp.JournalNest.entity.User;
import com.harsh.journalapp.JournalNest.enums.Mood;
import com.harsh.journalapp.JournalNest.mapper.JournalEntryMapper;
import com.harsh.journalapp.JournalNest.repository.JournalEntryRepository;
import com.harsh.journalapp.JournalNest.repository.JournalEntryRepositoryImplementation;
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
import java.util.ArrayList;
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

    @Autowired
    private JournalEntryRepositoryImplementation journalEntryRepositoryImplementation;

//    Create a new Journal Entry
    @Transactional
    public JournalEntry createJournalEntry(JournalEntryDTO journalEntryDTO, String username){
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

        return journalEntry1;
    }


//    GET Single Journal Entry
    public JournalEntry getJournalEntry(String journalEntryId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<JournalEntry> journalEntry = journalEntryRepository.findById(journalEntryId);

        if(journalEntry.isPresent()){
            JournalEntry journalEntry1 = journalEntry.get();
            User user = journalEntry1.getUser();
            if(user != null){
               if(user.getUsername().equals(username)){
                   return journalEntry1;
               }
            }
        }

        return null;
    }


//    GET All Journal Entries
    public Page<JournalEntry> getAllJournalEntries(int page){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRespository.findByUsername(username);

        if(user != null){
            Pageable pageable = PageRequest.of(page - 1 ,10, Sort.by("createdAt").descending());
            Page<JournalEntry> journalEntries = journalEntryRepository.findJournalEntriesByUser(user,pageable);
            return journalEntries;
        }
        return null;
    }


//    Update Single Journal Entry
    public String updateJournalEntry(String journalEntryId, JournalEntryDTO journalEntryDTO) {
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
                        return "Journal Entry Updated Successfully";

                    }
                }
            }

            return null;

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid mood or tags");
        }
    }



//    Delete Single Journal Entry
    public String deleteJournalEntry(String journalEntryId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<JournalEntry> journalEntry = journalEntryRepository.findById(journalEntryId);

        if(journalEntry.isPresent()){
            User user = journalEntry.get().getUser();

            if(user != null && username != null){
                if(user.getUsername().equals(username)){
                    journalEntryRepository.deleteById(journalEntryId);
                    return "Journal Entry Deleted Successfully";
                }
            }
        }
        return null;
    }


//    Get Filter Journal Entries
    public List<JournalEntry> getFilterJournalEntries(String search, String tag, String mood) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRespository.findByUsername(username);

        List<JournalEntry> filterJournalEntries = journalEntryRepositoryImplementation.getFilterJournalEntries(search, tag, mood, user);
        if(!filterJournalEntries.isEmpty()){
            return filterJournalEntries;
        }

        return null;
    }

//    Starred Journal Entries
    public String starredJournalEntry(String id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<JournalEntry> journalEntry = journalEntryRepository.findById(id);

        if(journalEntry.isPresent()){
            User user = journalEntry.get().getUser();

            if(user != null && username != null){
                if(user.getUsername().equals(username)){
                   JournalEntry journalEntry1 = journalEntry.get();
                   journalEntry1.setStarred(!journalEntry1.isStarred());
                   journalEntryRepository.save(journalEntry1);
                    return "Journal Entry Starred Successfully";
                }
            }
        }
        return null;
    }

//    Get Starred Entries
    public List<JournalEntry> getStarredEntries(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRespository.findByUsername(username);

        List<JournalEntry> journalEntries = journalEntryRepository.findJournalEntriesByUser(user);

        List<JournalEntry> starredEntries = new ArrayList<>();

        for(JournalEntry journalEntry : journalEntries){
            System.out.println(journalEntry.isStarred());
            if(journalEntry.isStarred()){
                System.out.println(journalEntry.getTitle());
                starredEntries.add(journalEntry);
            }
        }



        if(!starredEntries.isEmpty()){
            return starredEntries;
        }

        return null;
    }
}





