package com.cafebuddy.dto;

public class BookingRequest {

    private Long cafeId;
    private String startTime; // ISO-8601, e.g. "2025-06-01T10:00:00Z"
    private int people;

    public Long getCafeId() { return cafeId; }
    public void setCafeId(Long cafeId) { this.cafeId = cafeId; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public int getPeople() { return people; }
    public void setPeople(int people) { this.people = people; }
}
