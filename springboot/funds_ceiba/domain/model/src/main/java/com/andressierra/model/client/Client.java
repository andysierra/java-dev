package com.andressierra.model.client;

import com.andressierra.model.client.enums.NotificationPreferenceEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private String id;
    private String name;
    private String email;
    private String phone;
    private BigDecimal balance;
    private NotificationPreferenceEnum notificationPreference;
    private Long version;
}