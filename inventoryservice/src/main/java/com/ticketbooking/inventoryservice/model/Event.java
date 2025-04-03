package com.ticketbooking.inventoryservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "total_capacity")
    private Long totalCapacity;

    @Column(name = "left_capacity")
    private Long leftCapacity;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @Column(name = "ticket_price")
    private BigDecimal ticketPrice;
}
