package com.example.presidenttracker.component;

import com.example.presidenttracker.model.Itinerary;
import com.example.presidenttracker.model.Leader;
import com.example.presidenttracker.service.ItineraryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ItineraryService itineraryService;

    public DataSeeder(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Joe Biden
        Leader biden = new Leader();
        biden.setName("Joe Biden");
        biden.setCountry("USA");
        biden.setPosition("Former President");
        itineraryService.addLeader(biden);

        Itinerary bidenTrip1 = new Itinerary();
        bidenTrip1.setLeaderId(biden.getId());
        bidenTrip1.setStartLocation("Washington D.C.");
        bidenTrip1.setEndLocation("Washington D.C.");
        bidenTrip1.setStartDate(LocalDateTime.of(2025, 11, 20, 10, 0));
        bidenTrip1.setEndDate(LocalDateTime.of(2025, 11, 20, 14, 0));
        bidenTrip1.setDescription("Attending memorial service for Dick Cheney.");
        bidenTrip1.setStartLat(38.9072);
        bidenTrip1.setStartLng(-77.0369);
        bidenTrip1.setEndLat(38.9072);
        bidenTrip1.setEndLng(-77.0369);
        itineraryService.addItinerary(bidenTrip1);

        // Xi Jinping
        Leader xi = new Leader();
        xi.setName("Xi Jinping");
        xi.setCountry("China");
        xi.setPosition("President");
        itineraryService.addLeader(xi);

        Itinerary xiTrip1 = new Itinerary();
        xiTrip1.setLeaderId(xi.getId());
        xiTrip1.setStartLocation("Beijing");
        xiTrip1.setEndLocation("Sanya");
        xiTrip1.setStartDate(LocalDateTime.of(2025, 11, 7, 9, 0));
        xiTrip1.setEndDate(LocalDateTime.of(2025, 11, 7, 18, 0));
        xiTrip1.setDescription("Commissioning ceremony of Aircraft Carrier Fujian.");
        xiTrip1.setStartLat(39.9042);
        xiTrip1.setStartLng(116.4074);
        xiTrip1.setEndLat(18.2528);
        xiTrip1.setEndLng(109.5120);
        itineraryService.addItinerary(xiTrip1);

        Itinerary xiTrip2 = new Itinerary();
        xiTrip2.setLeaderId(xi.getId());
        xiTrip2.setStartLocation("Sanya");
        xiTrip2.setEndLocation("Beijing");
        xiTrip2.setStartDate(LocalDateTime.of(2025, 11, 8, 9, 0));
        xiTrip2.setEndDate(LocalDateTime.of(2025, 11, 8, 12, 0));
        xiTrip2.setDescription("Return to Beijing.");
        xiTrip2.setStartLat(18.2528);
        xiTrip2.setStartLng(109.5120);
        xiTrip2.setEndLat(39.9042);
        xiTrip2.setEndLng(116.4074);
        itineraryService.addItinerary(xiTrip2);

        // Emmanuel Macron
        Leader macron = new Leader();
        macron.setName("Emmanuel Macron");
        macron.setCountry("France");
        macron.setPosition("President");
        itineraryService.addLeader(macron);

        Itinerary macronTrip1 = new Itinerary();
        macronTrip1.setLeaderId(macron.getId());
        macronTrip1.setStartLocation("Paris");
        macronTrip1.setEndLocation("Brasilia");
        macronTrip1.setStartDate(LocalDateTime.of(2025, 11, 5, 8, 0));
        macronTrip1.setEndDate(LocalDateTime.of(2025, 11, 6, 20, 0));
        macronTrip1.setDescription("Visit to Brazil for COP30.");
        macronTrip1.setStartLat(48.8566);
        macronTrip1.setStartLng(2.3522);
        macronTrip1.setEndLat(-15.8267);
        macronTrip1.setEndLng(-47.9218);
        itineraryService.addItinerary(macronTrip1);

        Itinerary macronTrip2 = new Itinerary();
        macronTrip2.setLeaderId(macron.getId());
        macronTrip2.setStartLocation("Paris");
        macronTrip2.setEndLocation("Berlin");
        macronTrip2.setStartDate(LocalDateTime.of(2025, 11, 18, 9, 0));
        macronTrip2.setEndDate(LocalDateTime.of(2025, 11, 18, 18, 0));
        macronTrip2.setDescription("Summit on European Digital Sovereignty.");
        macronTrip2.setStartLat(48.8566);
        macronTrip2.setStartLng(2.3522);
        macronTrip2.setEndLat(52.5200);
        macronTrip2.setEndLng(13.4050);
        itineraryService.addItinerary(macronTrip2);

        Itinerary macronTrip3 = new Itinerary();
        macronTrip3.setLeaderId(macron.getId());
        macronTrip3.setStartLocation("Paris");
        macronTrip3.setEndLocation("Johannesburg");
        macronTrip3.setStartDate(LocalDateTime.of(2025, 11, 22, 10, 0));
        macronTrip3.setEndDate(LocalDateTime.of(2025, 11, 23, 20, 0));
        macronTrip3.setDescription("G20 Summit in South Africa.");
        macronTrip3.setStartLat(48.8566);
        macronTrip3.setStartLng(2.3522);
        macronTrip3.setEndLat(-26.2041);
        macronTrip3.setEndLng(28.0473);
        itineraryService.addItinerary(macronTrip3);

        // Narendra Modi
        Leader modi = new Leader();
        modi.setName("Narendra Modi");
        modi.setCountry("India");
        modi.setPosition("Prime Minister");
        itineraryService.addLeader(modi);

        Itinerary modiTrip1 = new Itinerary();
        modiTrip1.setLeaderId(modi.getId());
        modiTrip1.setStartLocation("New Delhi");
        modiTrip1.setEndLocation("Johannesburg");
        modiTrip1.setStartDate(LocalDateTime.of(2025, 11, 21, 8, 0));
        modiTrip1.setEndDate(LocalDateTime.of(2025, 11, 23, 22, 0));
        modiTrip1.setDescription("G20 Summit in South Africa.");
        modiTrip1.setStartLat(28.6139);
        modiTrip1.setStartLng(77.2090);
        modiTrip1.setEndLat(-26.2041);
        modiTrip1.setEndLng(28.0473);
        itineraryService.addItinerary(modiTrip1);
    }
}
