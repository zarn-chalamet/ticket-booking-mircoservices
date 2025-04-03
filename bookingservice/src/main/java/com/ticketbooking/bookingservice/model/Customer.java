package com.ticketbooking.bookingservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "customer")
public class Customer {
    @Id
    private Long id;
    private String name;
    private String email;
    private String address;
}
