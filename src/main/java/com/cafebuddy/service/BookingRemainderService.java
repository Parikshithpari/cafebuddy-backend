package com.cafebuddy.service;

import com.cafebuddy.model.Booking;
import com.cafebuddy.repository.BookingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class BookingRemainderService {

	@Autowired
    private BookingRepository bookingRepository;
	
	@Autowired
    private EmailService emailService;

    // Runs every minute
	@Transactional
	@Scheduled(fixedRate = 60000)
	public void sendExpiryReminders() {

	    Instant now = Instant.now();

	    Instant tenMinutesLater = now.plusSeconds(600);

	    List<Booking> bookings =
	            bookingRepository.findBookingsEndingSoon(
	                    now,
	                    tenMinutesLater
	            );

	    for (Booking booking : bookings) {

	        try {

	            emailService.sendExpiryReminder(
	                    booking.getUser(),
	                    booking,
	                    booking.getCafe()
	            );

	            booking.setReminderSent(true);

	            bookingRepository.save(booking);

	            System.out.println(
	                    "Reminder sent for booking: "
	                            + booking.getId()
	            );

	        } catch (Exception e) {

	            System.out.println(
	                    "Failed reminder for booking: "
	                            + booking.getId()
	            );

	            e.printStackTrace();
	        }
	    }
	}
}
