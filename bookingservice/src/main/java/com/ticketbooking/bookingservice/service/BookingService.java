package com.ticketbooking.bookingservice.service;

import com.ticketbooking.bookingservice.request.BookingRequest;
import com.ticketbooking.bookingservice.response.BookingResponse;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request);
}
