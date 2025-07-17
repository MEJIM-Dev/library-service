package com.me.library_service.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationType {

    REMINDER("REMINDER"),
    OVERDUE("OVERDUE"),;

    private final String notificationType;

}
