package com.example.presidenttracker.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Itinerary {
    private Long id;
    private Long leaderId;
    private String startLocation;
    private String endLocation;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;
    private Double startLat;
    private Double startLng;
    private Double endLat;
    private Double endLng;

    // Joined field
    private Leader leader;
}
