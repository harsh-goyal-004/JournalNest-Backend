package com.harsh.journalapp.JournalNest.repository;

import com.harsh.journalapp.JournalNest.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRespository extends MongoRepository<User,String> {
    User findByUsername(String username);
}
