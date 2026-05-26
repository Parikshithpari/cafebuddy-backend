package com.cafebuddy.controller;

import com.cafebuddy.dto.BookingDto;
import com.cafebuddy.dto.BookingRequest;
import com.cafebuddy.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /** POST /api/bookings — create a booking (authenticated) */
    @PostMapping
    public ResponseEntity<BookingDto> create(
            @RequestBody BookingRequest req,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(bookingService.createBooking(req, principal.getUsername()));
    }

    /** GET /api/bookings/mine — bookings for current user */
    @GetMapping("/mine")
    public ResponseEntity<List<BookingDto>> mine(
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(bookingService.getMyBookings(principal.getUsername()));
    }

    /** GET /api/bookings — all bookings (admin) */
    @GetMapping
    public ResponseEntity<List<BookingDto>> all() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    /** GET /api/bookings/cafe/{cafeId} — bookings for a cafe (admin) */
    @GetMapping("/cafe/{cafeId}")
    public ResponseEntity<List<BookingDto>> forCafe(@PathVariable Long cafeId) {
        return ResponseEntity.ok(bookingService.getBookingsForCafe(cafeId));
    }

    /** POST /api/bookings/{id}/extend — extend booking by 1 hour */
    @PostMapping("/{id}/extend")
    public ResponseEntity<BookingDto> extend(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(bookingService.extendBooking(id, principal.getUsername()));
    }
}
