package com.example.presidenttracker.dto;

import lombok.Data;

@Data
public class LeaderUpdateDto {
    private String leader_name;
    private String position;
    private String current_location;
    private String description;
    private String start_date;
    private String end_date;
    private double latitude;
    private double longitude;
    private String country;
}
