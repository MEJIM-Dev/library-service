package com.me.library_service.config;

import com.me.library_service.model.notification.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendNotification(NotificationMessage message) {
        rabbitTemplate.convertAndSend("notification.queue", message);
    }

}
