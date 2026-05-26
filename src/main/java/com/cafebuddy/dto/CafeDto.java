package com.cafebuddy.dto;

public class CafeDto {
    private Long id;
    private String name;
    private String area;
    private String address;
    private Double lat;
    private Double lng;
    private String mood;
    private String wifi;
    private String outlets;
    private Integer here;

    public CafeDto() {}

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

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name, area, address, mood, wifi, outlets;
        private Double lat, lng;
        private Integer here;

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

        public CafeDto build() {
            CafeDto dto = new CafeDto();
            dto.id = this.id; dto.name = this.name; dto.area = this.area;
            dto.address = this.address; dto.lat = this.lat; dto.lng = this.lng;
            dto.mood = this.mood; dto.wifi = this.wifi; dto.outlets = this.outlets;
            dto.here = this.here;
            return dto;
        }
    }
}
