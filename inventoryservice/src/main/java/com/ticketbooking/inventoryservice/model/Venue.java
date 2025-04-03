package com.ticketbooking.inventoryservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "venue")
public class Venue {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "total_capacity")
    private Long totalCapacity;
}
