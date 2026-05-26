package com.cafebuddy.model;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id", nullable = false)
    private Cafe cafe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private Instant endTime;

    @Column(nullable = false)
    private int people;

    @Column(nullable = false)
    private String status = "CONFIRMED"; // CONFIRMED, EXTENDED, CANCELLED
    
    @Column(nullable = false)
    private boolean reminderSent = false;

    // ── Getters & Setters ──────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cafe getCafe() { return cafe; }
    public void setCafe(Cafe cafe) { this.cafe = cafe; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Instant getStartTime() { return startTime; }
    public void setStartTime(Instant startTime) { this.startTime = startTime; }

    public Instant getEndTime() { return endTime; }
    public void setEndTime(Instant endTime) { this.endTime = endTime; }

    public int getPeople() { return people; }
    public void setPeople(int people) { this.people = people; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
	public boolean isReminderSent() {
		return reminderSent;
	}
	public void setReminderSent(boolean reminderSent) {
		this.reminderSent = reminderSent;
	}
    
    
}
