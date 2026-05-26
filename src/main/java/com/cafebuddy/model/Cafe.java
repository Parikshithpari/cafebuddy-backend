package com.cafebuddy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "cafes")
public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String name;

    @NotBlank(message = "Area is required")
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String area;

    @Size(max = 240)
    @Column(length = 240)
    private String address;

    private Double lat;
    private Double lng;

    @Column(length = 20)
    private String mood;

    @Column(length = 20)
    private String wifi;

    @Column(length = 20)
    private String outlets;

    @Column(name = "here_count")
    private Integer here = 0;

    public Cafe() {}

    public Cafe(Long id, String name, String area, String address,
                Double lat, Double lng, String mood, String wifi,
                String outlets, Integer here) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.mood = mood;
        this.wifi = wifi;
        this.outlets = outlets;
        this.here = here;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getArea() { return area; }
    public String getAddress() { return address; }
    public Double getLat() { return lat; }
    public Double getLng() { return lng; }
    public String getMood() { return mood; }
    public String getWifi() { return wifi; }
    public String getOutlets() { return outlets; }
    public Integer getHere() { return here; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setArea(String area) { this.area = area; }
    public void setAddress(String address) { this.address = address; }
    public void setLat(Double lat) { this.lat = lat; }
    public void setLng(Double lng) { this.lng = lng; }
    public void setMood(String mood) { this.mood = mood; }
    public void setWifi(String wifi) { this.wifi = wifi; }
    public void setOutlets(String outlets) { this.outlets = outlets; }
    public void setHere(Integer here) { this.here = here; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name, area, address, mood, wifi, outlets;
        private Double lat, lng;
        private Integer here = 0;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder area(String area) { this.area = area; return this; }
        public Builder address(String address) { this.address = address; return this; }
        public Builder lat(Double lat) { this.lat = lat; return this; }
        public Builder lng(Double lng) { this.lng = lng; return this; }
        public Builder mood(String mood) { this.mood = mood; return this; }
        public Builder wifi(String wifi) { this.wifi = wifi; return this; }
        public Builder outlets(String outlets) { this.outlets = outlets; return this; }
        public Builder here(Integer here) { this.here = here; return this; }

        public Cafe build() {
            return new Cafe(id, name, area, address, lat, lng, mood, wifi, outlets, here);
        }
    }
}
