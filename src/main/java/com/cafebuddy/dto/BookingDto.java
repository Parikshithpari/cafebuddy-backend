package com.cafebuddy.dto;

public class BookingDto {

    private Long id;
    private Long cafeId;
    private String cafeName;
    private Long userId;
    private String userFullName;
    private String userEmail;
    private String startTime;
    private String endTime;
    private int people;
    private String status;

    // ── Getters & Setters ──────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCafeId() { return cafeId; }
    public void setCafeId(Long cafeId) { this.cafeId = cafeId; }

    public String getCafeName() { return cafeName; }
    public void setCafeName(String cafeName) { this.cafeName = cafeName; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public int getPeople() { return people; }
    public void setPeople(int people) { this.people = people; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
