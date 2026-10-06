package com.andressierra.api.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscribeRequest {

    @NotBlank(message = "El clientId es obligatorio")
    private String clientId;

    @NotNull(message = "El fundId es obligatorio")
    private Long fundId;

    @NotBlank(message = "La preferencia de notificacion es obligatoria")
    private String notificationPreference;
}