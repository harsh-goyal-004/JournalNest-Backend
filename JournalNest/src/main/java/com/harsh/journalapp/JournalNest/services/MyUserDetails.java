package com.harsh.journalapp.JournalNest.services;

import com.harsh.journalapp.JournalNest.entity.User;
import com.harsh.journalapp.JournalNest.repository.UserRespository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MyUserDetails  implements UserDetailsService {

    @Autowired
    private UserRespository userRespository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User dbUser = userRespository.findByUsername(username);

        if(dbUser != null){
            return org.springframework.security.core.userdetails.User
                    .builder()
                    .username(dbUser.getUsername())
                    .password(dbUser.getPassword())
                    .roles(String.valueOf(dbUser.getRoles()))
                    .build();
        }

        log.error("user not found in MyUserDetailsService");
        throw new UsernameNotFoundException("User not Found!!!");

    }
}
