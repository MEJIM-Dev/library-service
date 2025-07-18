package com.me.library_service.config;

import com.me.library_service.model.notification.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.queue.exchange}")
    public String NOTIFICATION_QUEUE;

    public void sendNotification(NotificationMessage message) {
        log.info("[Q] Sending notification: {}", message);
        rabbitTemplate.convertAndSend(NOTIFICATION_QUEUE, message);
        log.info("[Q] Notification Sent");
    }

}
