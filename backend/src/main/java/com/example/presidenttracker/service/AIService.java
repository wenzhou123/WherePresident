package com.example.presidenttracker.service;

import com.example.presidenttracker.dto.LeaderUpdateDto;
import com.example.presidenttracker.model.Itinerary;
import com.example.presidenttracker.model.Leader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AIService {

    private static final Logger logger = LoggerFactory.getLogger(AIService.class);
    private final RestTemplate restTemplate;
    private final ItineraryService itineraryService;
    @org.springframework.beans.factory.annotation.Value("${ai.service.url:http://localhost:8000/api/track-leader}")
    private String langServerUrl;

    public AIService(ItineraryService itineraryService) {
        this.restTemplate = new RestTemplate();
        this.itineraryService = itineraryService;
    }

    public void updateLeaderInfo(String country) {
        logger.info("Requesting AI update for country: {}", country);
        Map<String, String> request = new HashMap<>();
        request.put("country", country);

        try {
            logger.info("Sending request to LangServer: {}", langServerUrl);
            LeaderUpdateDto response = restTemplate.postForObject(langServerUrl, request, LeaderUpdateDto.class);

            if (response != null) {
                logger.info("Received response from LangServer: {}", response);
                processLeaderUpdate(response, country);
            } else {
                logger.error("Received null response from LangServer for country: {}", country);
            }
        } catch (Exception e) {
            logger.error("Error calling AI service for country: {}", country, e);
            throw new RuntimeException("Failed to update leader info via AI", e);
        }
    }

    private void processLeaderUpdate(LeaderUpdateDto dto, String country) {
        logger.info("Processing leader update for: {}", dto.getLeader_name());

        // 1. Find or Create Leader
        Leader leader = itineraryService.getLeaderByName(dto.getLeader_name());
        if (leader == null) {
            logger.info("Leader not found, creating new leader: {}", dto.getLeader_name());
            leader = new Leader();
            leader.setName(dto.getLeader_name());
            leader.setCountry(country);
            leader.setPosition(dto.getPosition());
            itineraryService.addLeader(leader);
        } else {
            logger.info("Found existing leader: {}, updating info", leader.getName());
            // Update existing leader info
            leader.setCountry(country);
            leader.setPosition(dto.getPosition());
            itineraryService.updateLeader(leader);
        }

        // 2. Create Itinerary
        Itinerary itinerary = new Itinerary();
        itinerary.setLeader(leader);
        itinerary.setLeaderId(leader.getId()); // Explicitly set the foreign key
        itinerary.setStartLocation(dto.getCurrent_location()); // Simplified: using current loc as start
        itinerary.setEndLocation(dto.getCurrent_location()); // and end for now, or infer from description
        itinerary.setStartDate(LocalDateTime.now());
        itinerary.setEndDate(LocalDateTime.now().plusDays(1));
        itinerary.setDescription(dto.getDescription());

        // Set coordinates if available
        // DTO uses primitive double, so they can't be null, but 0.0 might indicate
        // missing
        if (dto.getLatitude() != 0.0)
            itinerary.setStartLat(dto.getLatitude());
        if (dto.getLongitude() != 0.0)
            itinerary.setStartLng(dto.getLongitude());
        if (dto.getLatitude() != 0.0)
            itinerary.setEndLat(dto.getLatitude());
        if (dto.getLongitude() != 0.0)
            itinerary.setEndLng(dto.getLongitude());

        itineraryService.saveOrUpdateItinerary(itinerary);
        logger.info("Successfully saved/updated itinerary for {}", leader.getName());
    }
}
