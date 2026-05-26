package com.cafebuddy.service;

import com.cafebuddy.dto.BookingDto;
import com.cafebuddy.dto.BookingRequest;
import com.cafebuddy.model.Booking;
import com.cafebuddy.model.Cafe;
import com.cafebuddy.model.User;
import com.cafebuddy.repository.BookingRepository;
import com.cafebuddy.repository.CafeRepository;
import com.cafebuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired private BookingRepository bookingRepository;
    @Autowired private CafeRepository cafeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;

    /** Create a new booking (1-hour slot). */
    public BookingDto createBooking(BookingRequest req, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // CHECK ACTIVE BOOKING
        bookingRepository.findByUserIdAndEndTimeAfter(user.getId(), Instant.now())
                .ifPresent(b -> {
                    throw new RuntimeException(
                            "You already have an active booking. " +
                            "Please complete or extend it before booking another cafe."
                    );
                });

        Cafe cafe = cafeRepository.findById(req.getCafeId())
                .orElseThrow(() -> new RuntimeException("Cafe not found"));

        Instant start = Instant.parse(req.getStartTime());
        Instant end = start.plusSeconds(3600);

        if (req.getPeople() < 1 || req.getPeople() > 20) {
            throw new IllegalArgumentException("People must be between 1 and 20");
        }
        
        if (start.isBefore(Instant.now())) {
            throw new IllegalArgumentException(
                    "Booking time cannot be in the past"
            );
        }

        Booking booking = new Booking();
        booking.setCafe(cafe);
        booking.setUser(user);
        booking.setStartTime(start);
        booking.setEndTime(end);
        booking.setPeople(req.getPeople());
        booking.setStatus("CONFIRMED");

        Booking saved = bookingRepository.save(booking);

        cafe.setHere(cafe.getHere() + req.getPeople());
        cafeRepository.save(cafe);

        try {
            emailService.sendBookingConfirmation(user, saved, cafe);
        } catch (Exception ignored) {}

        return toDto(saved);
    }

    /** Get all bookings for the authenticated user. */
    public List<BookingDto> getMyBookings(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return bookingRepository.findByUserIdOrderByStartTimeDesc(user.getId())
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    /** Get all bookings (admin). */
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    /** Get all bookings for a specific cafe (admin). */
    public List<BookingDto> getBookingsForCafe(Long cafeId) {
        return bookingRepository.findByCafeIdOrderByStartTimeDesc(cafeId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    /** Extend a booking by 1 hour. */
    public BookingDto extendBooking(Long bookingId, String userEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Not authorized to extend this booking");
        }

        booking.setEndTime(booking.getEndTime().plusSeconds(3600));
        booking.setStatus("EXTENDED");
        return toDto(bookingRepository.save(booking));
    }

    // ── Mapper ────────────────────────────────────────────────────────────

    private BookingDto toDto(Booking b) {
        BookingDto dto = new BookingDto();
        dto.setId(b.getId());
        dto.setCafeId(b.getCafe().getId());
        dto.setCafeName(b.getCafe().getName());
        dto.setUserId(b.getUser().getId());
        dto.setUserFullName(b.getUser().getFullName());
        dto.setUserEmail(b.getUser().getEmail());
        dto.setStartTime(b.getStartTime().toString());
        dto.setEndTime(b.getEndTime().toString());
        dto.setPeople(b.getPeople());
        dto.setStatus(b.getStatus());
        return dto;
    }
}
