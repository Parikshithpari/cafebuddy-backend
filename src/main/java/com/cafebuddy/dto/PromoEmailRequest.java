package com.cafebuddy.dto;

import jakarta.validation.constraints.NotBlank;

public class PromoEmailRequest {

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Message body is required")
    private String body;

    public PromoEmailRequest() {}

    public String getSubject() { return subject; }
    public String getBody() { return body; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setBody(String body) { this.body = body; }
}
