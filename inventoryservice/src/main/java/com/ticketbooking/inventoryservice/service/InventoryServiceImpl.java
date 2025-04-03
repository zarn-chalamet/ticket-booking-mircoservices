package com.ticketbooking.inventoryservice.service;

import com.ticketbooking.inventoryservice.model.Event;
import com.ticketbooking.inventoryservice.model.Venue;
import com.ticketbooking.inventoryservice.repository.EventRepository;
import com.ticketbooking.inventoryservice.repository.VenueRepository;
import com.ticketbooking.inventoryservice.response.EventInventoryResponse;
import com.ticketbooking.inventoryservice.response.VenueInventoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService{

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    @Autowired
    public InventoryServiceImpl(EventRepository eventRepository, VenueRepository venueRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }

    @Override
    public List<EventInventoryResponse> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(event -> EventInventoryResponse.builder()
                .eventId(event.getId())
                .event(event.getName())
                .capacity(event.getLeftCapacity())
                .venue(event.getVenue())
                .ticketPrice(event.getTicketPrice())
                .build()).collect(Collectors.toList());
    }

    @Override
    public EventInventoryResponse getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found with id: "+eventId));
        return EventInventoryResponse.builder()
                .eventId(event.getId())
                .event(event.getName())
                .capacity(event.getLeftCapacity())
                .venue(event.getVenue())
                .ticketPrice(event.getTicketPrice())
                .build();
    }

    @Override
    public VenueInventoryResponse getVenueById(Long venueId) {
        Venue venue = venueRepository.findById(venueId).orElseThrow(() -> new RuntimeException("Venue not found with id: "+venueId));
        return VenueInventoryResponse.builder()
                .venueId(venue.getId())
                .venueName(venue.getName())
                .address(venue.getAddress())
                .totalCapacity(venue.getTotalCapacity())
                .build();
    }

    @Override
    public void updateEventLeftCapacity(Long eventId, Long ticketBooked) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found with id: "+eventId));
        event.setLeftCapacity(event.getLeftCapacity() - ticketBooked);
        eventRepository.saveAndFlush(event);
    }

}
