package com.ticketbooking.inventoryservice.service;

import com.ticketbooking.inventoryservice.response.EventInventoryResponse;
import com.ticketbooking.inventoryservice.response.VenueInventoryResponse;

import java.util.List;

public interface InventoryService {
    List<EventInventoryResponse> getAllEvents();

    EventInventoryResponse getEventById(Long eventId);

    VenueInventoryResponse getVenueById(Long venueId);

    void updateEventLeftCapacity(Long eventId, Long ticketBooked);
}
