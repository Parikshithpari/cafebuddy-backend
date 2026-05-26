package com.cafebuddy.service;

import com.cafebuddy.model.Booking;
import com.cafebuddy.model.Cafe;
import com.cafebuddy.model.User;
import com.cafebuddy.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Value("${app.mail.from}")
    private String fromEmail;

    /**
     * Sends a welcome email to a newly registered user.
     */
    @Async
    public void sendWelcomeEmail(String toEmail, String fullName) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromEmail);
            msg.setTo(toEmail);
            msg.setSubject("Welcome to Brewmates ☕");
            msg.setText(
                "Hi " + fullName + ",\n\n" +
                "Welcome to Brewmates! We're excited to have you on board.\n\n" +
                "Brewmates helps you find the perfect café for getting work done — " +
                "see who's there in real time, check WiFi quality, and vibe.\n\n" +
                "Start exploring cafés at: https://gconnectt.com/cafebuddy/cafes\n\n" +
                "Happy brewing,\n" +
                "The Brewmates Team"
            );
            mailSender.send(msg);
            logger.info("Welcome email sent to {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send welcome email to {}: {}", toEmail, e.getMessage());
        }
    }

    /**
     * Sends a promotional email to all registered users.
     * Returns the count of users emailed.
     */
    public int sendPromoToAllUsers(String subject, String body) {
        List<User> users = userRepository.findAll();
        int sent = 0;
        for (User user : users) {
            try {
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setFrom(fromEmail);
                msg.setTo(user.getEmail());
                msg.setSubject(subject);
                msg.setText("Hi " + user.getFullName() + ",\n\n" + body +
                             "\n\n— The Brewmates Team\n\nTo unsubscribe, reply to this email.");
                mailSender.send(msg);
                sent++;
                logger.info("Promo email sent to {}", user.getEmail());
            } catch (Exception e) {
                logger.error("Failed to send promo to {}: {}", user.getEmail(), e.getMessage());
            }
        }
        return sent;
    }
    
    public void sendBookingConfirmation(User user, Booking booking, Cafe cafe)
            throws MessagingException {

        String subject = "☕ Your Cafe Booking is Confirmed!";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
                .withZone(ZoneId.systemDefault());

        String startTime = formatter.format(booking.getStartTime());
        String endTime = formatter.format(booking.getEndTime());

        String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f4f4;
                            margin: 0;
                            padding: 0;
                        }

                        .container {
                            max-width: 600px;
                            margin: 40px auto;
                            background: #ffffff;
                            border-radius: 12px;
                            overflow: hidden;
                            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
                        }

                        .header {
                            background: linear-gradient(135deg, #6f4e37, #a67c52);
                            color: white;
                            text-align: center;
                            padding: 30px 20px;
                        }

                        .header h1 {
                            margin: 0;
                            font-size: 28px;
                        }

                        .content {
                            padding: 30px;
                            color: #333333;
                        }

                        .card {
                            background: #fafafa;
                            border-radius: 10px;
                            padding: 20px;
                            margin-top: 20px;
                            border-left: 5px solid #6f4e37;
                        }

                        .detail {
                            margin: 12px 0;
                            font-size: 15px;
                        }

                        .label {
                            font-weight: bold;
                            color: #6f4e37;
                        }

                        .status {
                            display: inline-block;
                            background: #d4edda;
                            color: #155724;
                            padding: 8px 14px;
                            border-radius: 20px;
                            font-size: 14px;
                            font-weight: bold;
                            margin-top: 15px;
                        }

                        .footer {
                            text-align: center;
                            padding: 20px;
                            background: #f9f9f9;
                            color: #777777;
                            font-size: 13px;
                        }

                        .btn {
                            display: inline-block;
                            margin-top: 20px;
                            padding: 12px 20px;
                            background: #6f4e37;
                            color: white;
                            text-decoration: none;
                            border-radius: 6px;
                            font-weight: bold;
                        }

                    </style>
                </head>
                <body>

                    <div class="container">

                        <div class="header">
                            <h1>☕ Booking Confirmed</h1>
                            <p>Your table is reserved successfully</p>
                        </div>

                        <div class="content">

                            <p>Hi <strong>%s</strong>,</p>

                            <p>
                                Thank you for booking with us! Your reservation has been confirmed.
                            </p>

                            <div class="card">

                                <div class="detail">
    <span class="label">Booking ID:</span> #%d
</div>

<div class="detail">
    <span class="label">Cafe:</span> %s
</div>

                                <div class="detail">
                                    <span class="label">People:</span> %d
                                </div>

                                <div class="detail">
                                    <span class="label">Start Time:</span> %s
                                </div>

                                <div class="detail">
                                    <span class="label">End Time:</span> %s
                                </div>

                                <div class="status">
                                    ✔ CONFIRMED
                                </div>

                            </div>

                            <p style="margin-top: 25px;">
                                We look forward to serving you. Have a great time at
                                <strong>%s</strong>!
                            </p>

                        </div>

                        <div class="footer">
                            © 2026 Cafe Booking System <br/>
                            Made with ☕ and Spring Boot
                        </div>

                    </div>

                </body>
                </html>
                """.formatted(
        user.getFullName(),
        booking.getId(),
        cafe.getName(),
        booking.getPeople(),
        startTime,
        endTime,
        cafe.getName()
);

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(html, true);

        mailSender.send(message);
    }
    
    public void sendExpiryReminder(User user, Booking booking, Cafe cafe)
            throws MessagingException {

        String subject = "⏰ Your booking ends in 10 minutes";

        String html = """
                <html>
                <body style="font-family:Arial;padding:20px;background:#f4f4f4;">

                    <div style="
                        max-width:600px;
                        margin:auto;
                        background:white;
                        border-radius:12px;
                        overflow:hidden;
                        box-shadow:0 2px 10px rgba(0,0,0,0.1);
                    ">

                        <div style="
                            background:#ff9800;
                            color:white;
                            padding:20px;
                            text-align:center;
                        ">
                            <h1>⏰ Booking Expiring Soon</h1>
                        </div>

                        <div style="padding:30px;">

                            <p>Hi <b>%s</b>,</p>

                            <p>
                                Your cafe booking will expire in
                                <b>10 minutes</b>.
                            </p>

                            <div style="
                                background:#fafafa;
                                padding:20px;
                                border-radius:10px;
                                margin-top:20px;
                            ">

                                <p><b>Booking ID:</b> #%d</p>
                                <p><b>Cafe:</b> %s</p>
                                <p><b>Current End Time:</b> %s</p>

                            </div>

                            <p style="margin-top:25px;">
                                If you'd like to continue your stay,
                                please extend your booking now.
                            </p>

                        </div>

                    </div>

                </body>
                </html>
                """.formatted(
                user.getFullName(),
                booking.getId(),
                cafe.getName(),
                booking.getEndTime().toString()
        );

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(message, true);

        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(html, true);

        mailSender.send(message);
    }
}
