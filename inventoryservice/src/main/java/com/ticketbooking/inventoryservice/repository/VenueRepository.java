package com.ticketbooking.inventoryservice.repository;

import com.ticketbooking.inventoryservice.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends JpaRepository<Venue,Long> {
}
