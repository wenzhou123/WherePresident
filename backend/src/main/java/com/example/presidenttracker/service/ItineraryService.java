package com.example.presidenttracker.service;

import com.example.presidenttracker.mapper.ItineraryMapper;
import com.example.presidenttracker.mapper.LeaderMapper;
import com.example.presidenttracker.model.Itinerary;
import com.example.presidenttracker.model.Leader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItineraryService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ItineraryService.class);

    private final ItineraryMapper itineraryMapper;
    private final LeaderMapper leaderMapper;

    public ItineraryService(ItineraryMapper itineraryMapper, LeaderMapper leaderMapper) {
        this.itineraryMapper = itineraryMapper;
        this.leaderMapper = leaderMapper;
    }

    public List<Itinerary> getAllItineraries() {
        List<Itinerary> itineraries = itineraryMapper.findAll();
        logger.info("Retrieved {} itineraries", itineraries.size());
        return itineraries;
    }

    @Transactional
    public void addItinerary(Itinerary itinerary) {
        logger.info("Adding itinerary for leader: {} from {} to {}",
                itinerary.getLeader() != null ? itinerary.getLeader().getName() : "Unknown",
                itinerary.getStartLocation(),
                itinerary.getEndLocation());
        itineraryMapper.insert(itinerary);
    }

    @Transactional
    public void saveOrUpdateItinerary(Itinerary itinerary) {
        Itinerary existing = itineraryMapper.findByLeaderId(itinerary.getLeaderId());
        if (existing != null) {
            logger.info("Updating existing itinerary for leader ID: {}", itinerary.getLeaderId());
            itinerary.setId(existing.getId()); // Preserve ID
            itineraryMapper.update(itinerary);
        } else {
            logger.info("Creating new itinerary for leader ID: {}", itinerary.getLeaderId());
            itineraryMapper.insert(itinerary);
        }
    }

    @Transactional
    public void addLeader(Leader leader) {
        logger.info("Adding/Updating leader: {}, Country: {}", leader.getName(), leader.getCountry());
        leaderMapper.insert(leader);
    }

    @Transactional
    public void updateLeader(Leader leader) {
        logger.info("Updating existing leader: {}, ID: {}", leader.getName(), leader.getId());
        leaderMapper.update(leader);
    }

    public Leader getLeaderByName(String name) {
        return leaderMapper.findByName(name);
    }
}
