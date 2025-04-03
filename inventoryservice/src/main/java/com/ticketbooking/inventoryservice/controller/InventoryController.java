package com.ticketbooking.inventoryservice.controller;

import com.ticketbooking.inventoryservice.model.Event;
import com.ticketbooking.inventoryservice.response.EventInventoryResponse;
import com.ticketbooking.inventoryservice.response.VenueInventoryResponse;
import com.ticketbooking.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/events")
    public @ResponseBody List<EventInventoryResponse> getAllEvents(){
        return inventoryService.getAllEvents();
    }

    @GetMapping("/event/{eventId}")
    public @ResponseBody EventInventoryResponse getEventByEventId(@PathVariable("eventId") Long eventId){
        return inventoryService.getEventById(eventId);
    }

    @GetMapping("/venue/{venueId}")
    public @ResponseBody VenueInventoryResponse getVenueByVenueId(@PathVariable("venueId") Long venueId){
        return inventoryService.getVenueById(venueId);
    }

    @PutMapping("/events/{eventId}/capacity/{capacity}")
    public ResponseEntity<Void> updateEventCapacity(@PathVariable("eventId") Long eventId,
                                                    @PathVariable("capacity") Long ticketBooked){
        inventoryService.updateEventLeftCapacity(eventId,ticketBooked);
        return ResponseEntity.ok().build();
    }
}
