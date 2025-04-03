package com.ticketbooking.bookingservice.service;

import com.ticketbooking.bookingservice.client.InventoryServiceClient;
import com.ticketbooking.bookingservice.event.BookingEvent;
import com.ticketbooking.bookingservice.model.Customer;
import com.ticketbooking.bookingservice.repository.CustomerRepository;
import com.ticketbooking.bookingservice.request.BookingRequest;
import com.ticketbooking.bookingservice.response.BookingResponse;
import com.ticketbooking.bookingservice.response.InventoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService{

    private final CustomerRepository customerRepository;
    private final InventoryServiceClient inventoryServiceClient;
    private final KafkaTemplate<String,BookingEvent> kafkaTemplate;

    @Autowired
    public BookingServiceImpl(CustomerRepository customerRepository, InventoryServiceClient inventoryServiceClient, KafkaTemplate<String, BookingEvent> kafkaTemplate) {
        this.customerRepository = customerRepository;
        this.inventoryServiceClient = inventoryServiceClient;
        this.kafkaTemplate = kafkaTemplate;
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
        final BookingEvent bookingEvent = createBookingEvent(request,customer,inventoryResponse);

        log.info("before kafa template: "+bookingEvent);
        //send booking to Order Service on a Kafka Topic
        kafkaTemplate.send("booking", bookingEvent);
        log.info("Booking sent to Kafka: {}",bookingEvent);

        return BookingResponse.builder()
                .userId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .ticketCount(bookingEvent.getTicketCount())
                .totalPrice(bookingEvent.getTotalPrice())
                .build();
    }

    private BookingEvent createBookingEvent(BookingRequest request,
                                            Customer customer,
                                            InventoryResponse inventoryResponse) {
        return BookingEvent.builder()
                .userId(customer.getId())
                .eventId(request.getEventId())
                .ticketCount(request.getTicketCount())
                .totalPrice(inventoryResponse.getTicketPrice().multiply(BigDecimal.valueOf(request.getTicketCount())))
                .build();
    }
}
