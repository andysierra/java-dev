package com.andressierra.usecase.subscribe;

import com.andressierra.usecase.Command;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubscribeCommand extends Command {
    private String clientId;
    private Long fundId;
    private String notificationPreference;
}
