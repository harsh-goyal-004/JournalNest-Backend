package com.harsh.journalapp.JournalNest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PublicController {

    @GetMapping("/health-check")
    public String healthCheck(){
        return "OK";
    }
}
