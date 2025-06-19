package com.harsh.journalapp.JournalNest.repository;

import com.harsh.journalapp.JournalNest.entity.JournalEntry;
import com.harsh.journalapp.JournalNest.entity.User;
import com.harsh.journalapp.JournalNest.enums.Mood;
import com.harsh.journalapp.JournalNest.enums.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class JournalEntryRepositoryImplementation {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<JournalEntry> getFilterJournalEntries(String search, List<String> tag, String mood, User user){

        Query query = new Query();
        Criteria criteria1 = new Criteria();
        List<Criteria> criteria = new ArrayList<>();

        if(search != null && !search.isEmpty()){
            criteria.add(
                    new Criteria().orOperator(
                            Criteria.where("title").regex(search,"i"),
                            Criteria.where("content").regex(search,"i")
                    )
            );
        }

        if(tag != null && !tag.isEmpty()){
           try{
               criteria.add(Criteria.where("tags").all(tag));
           }catch(IllegalArgumentException e){
               log.error("Tag is invalid");
           }
        }

        if(mood != "" && mood != null){
            try {
                criteria.add(Criteria.where("mood").is(Mood.valueOf(mood.toUpperCase())));
            }catch (IllegalArgumentException e){
                log.error("Mood is invalid");
            }
        }


        if(!criteria.isEmpty()){
            criteria.add(Criteria.where("user").is(user));
            query.addCriteria(criteria1.andOperator(criteria.toArray(new Criteria[0])));
            List<JournalEntry> journalEntries = mongoTemplate.find(query, JournalEntry.class);
            return journalEntries;
        }else{
            return List.of();
        }

    }
}
