package com.andressierra.usecase.cancel;

import com.andressierra.usecase.Command;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CancelCommand extends Command {
    private String clientId;
    private Long fundId;
}
