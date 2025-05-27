package com.harsh.journalapp.JournalNest.controller;

import com.harsh.journalapp.JournalNest.dto.AnalyticsDTO;
import com.harsh.journalapp.JournalNest.services.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/summary")
    public ResponseEntity<?> getAnalyticsSummary(){
        AnalyticsDTO analyticsSummary = analyticsService.getAnalyticsSummary();

        if(analyticsSummary != null){
            return new ResponseEntity<>(analyticsSummary, HttpStatus.OK);
        }

        return new ResponseEntity<>("No user found", HttpStatus.NOT_FOUND);
    }
}
