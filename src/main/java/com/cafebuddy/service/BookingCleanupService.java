package com.cafebuddy.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cafebuddy.model.Booking;
import com.cafebuddy.repository.BookingRepository;

@Service
public class BookingCleanupService {

	@Autowired
    private BookingRepository bookingRepository;

    @Scheduled(fixedRate = 60000)
    public void expireBookings() {

        Instant now = Instant.now();

        List<Booking> expired =
                bookingRepository.findExpiredBookings(now);

        for (Booking booking : expired) {
            booking.setStatus("COMPLETED");
        }

        bookingRepository.saveAll(expired);
    }
}
