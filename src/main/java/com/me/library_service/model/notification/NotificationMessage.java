package com.me.library_service.model.notification;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationMessage {

    private String type;
    private String to;
    private String subject;
    private String body;
    private NotificationMeta metadata;

}