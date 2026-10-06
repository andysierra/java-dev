package co.com.asierra.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CrearProductoRequest {

    private String nombre;

    private Integer sucursalId;

    private Integer status;
}
