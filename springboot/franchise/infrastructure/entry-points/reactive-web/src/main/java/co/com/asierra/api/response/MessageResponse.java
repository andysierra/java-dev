package co.com.asierra.api.response;

import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class MessageResponse {
    private ZonedDateTime timestamp;
    private int status;
    private String error;
    private String message;
}
