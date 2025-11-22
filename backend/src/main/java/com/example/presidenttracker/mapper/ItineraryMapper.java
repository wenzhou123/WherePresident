package com.example.presidenttracker.mapper;

import com.example.presidenttracker.model.Itinerary;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ItineraryMapper {
    List<Itinerary> findAll();

    void insert(Itinerary itinerary);

    void update(Itinerary itinerary);

    Itinerary findByLeaderId(Long leaderId);
}
