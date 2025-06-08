package org.thivernale.notificationservice.domain;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thivernale.notificationservice.ApplicationProperties;
import org.thivernale.notificationservice.domain.models.OrderCancelledEvent;
import org.thivernale.notificationservice.domain.models.OrderCreatedEvent;
import org.thivernale.notificationservice.domain.models.OrderDeliveredEvent;
import org.thivernale.notificationservice.domain.models.OrderErrorEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final JavaMailSender mailSender;
    private final ApplicationProperties applicationProperties;

    public void sendOrderCreatedNotification(OrderCreatedEvent event) {
        String body = """
            Order Created Notification
            
            Dear %s,
            Your order with # %s has been created successfully!
            
            Regards,
            Bookstore Team
            """.formatted(event.customer()
            .name(), event.orderNumber());
        sendMail(event.customer()
            .email(), "Order Created Notification", body);
    }

    public void sendOrderDeliveredNotification(OrderDeliveredEvent event) {
        String body = """
            Order Delivered Notification
            
            Dear %s,
            Your order with # %s has been delivered successfully!
            
            Regards,
            Bookstore Team
            """.formatted(event.customer()
            .name(), event.orderNumber());
        sendMail(event.customer()
            .email(), "Order Delivered Notification", body);
    }

    public void sendOrderCancelledNotification(OrderCancelledEvent event) {
        String body = """
            Order Cancelled Notification
            
            Dear %s,
            Your order with # %s has been cancelled!
            Reason: %s
            
            Regards,
            Bookstore Team
            """.formatted(event.customer()
            .name(), event.orderNumber(), event.reason());
        sendMail(event.customer()
            .email(), "Order Cancelled Notification", body);
    }

    public void sendErrorCreateNotification(OrderErrorEvent event) {
        String body = """
            Order Error Notification
            
            Dear Support Team,
            There was an error processing order with # %s.
            Details: %s
            
            Regards,
            Bookstore Team
            """.formatted(event.orderNumber(), event.reason());
        sendMail(applicationProperties.supportEmail(), "Order Error Notification", body);
    }

    private void sendMail(String to, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(applicationProperties.supportEmail());
            helper.addTo(to);
            helper.setSubject(subject);
            helper.setText(body);

            mailSender.send(mimeMessage);
            log.info("Mail sent to {}", to);
        } catch (MessagingException e) {
            throw new RuntimeException("Error while sending email", e);
        }
    }
}
