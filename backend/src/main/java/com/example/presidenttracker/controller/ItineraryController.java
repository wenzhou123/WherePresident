package com.example.presidenttracker.controller;

import com.example.presidenttracker.model.Itinerary;
import com.example.presidenttracker.service.AIService;
import com.example.presidenttracker.service.ItineraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/itineraries")
public class ItineraryController {

    private static final Logger logger = LoggerFactory.getLogger(ItineraryController.class);
    private final ItineraryService itineraryService;
    private final AIService aiService;

    public ItineraryController(ItineraryService itineraryService, AIService aiService) {
        this.itineraryService = itineraryService;
        this.aiService = aiService;
    }

    @GetMapping
    public List<Itinerary> getAllItineraries() {
        logger.info("Fetching all itineraries");
        return itineraryService.getAllItineraries();
    }

    @PostMapping("/update")
    public void updateLeaderInfo(@RequestBody Map<String, String> payload) {
        String country = payload.get("country");
        logger.info("Received update request for country: {}", country);
        if (country != null) {
            aiService.updateLeaderInfo(country);
        } else {
            logger.warn("Update request missing 'country' parameter");
        }
    }
}
