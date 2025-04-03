package com.ticketbooking.bookingservice.service;

import com.ticketbooking.bookingservice.client.InventoryServiceClient;
import com.ticketbooking.bookingservice.model.Customer;
import com.ticketbooking.bookingservice.repository.CustomerRepository;
import com.ticketbooking.bookingservice.request.BookingRequest;
import com.ticketbooking.bookingservice.response.BookingResponse;
import com.ticketbooking.bookingservice.response.InventoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService{

    private final CustomerRepository customerRepository;
    private final InventoryServiceClient inventoryServiceClient;

    @Autowired
    public BookingServiceImpl(CustomerRepository customerRepository, InventoryServiceClient inventoryServiceClient) {
        this.customerRepository = customerRepository;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    @Override
    public BookingResponse createBooking(BookingRequest request) {
        //check if the user exists
        Customer customer = customerRepository.findById(request.getUserId()).orElseThrow(()-> new RuntimeException("User not found"));

        //check if there is enough inventory capacity
        final InventoryResponse inventoryResponse = inventoryServiceClient.getInventory(request.getEventId());
        log.info("Inventory Response: {}", inventoryResponse);
        if (inventoryResponse.getCapacity() < request.getTicketCount()){
            throw  new RuntimeException("Not enough inventory");
        }

        //create booking
        final B

        //send booking to Order Service on a Kafka Topic

        return null;
    }
}
