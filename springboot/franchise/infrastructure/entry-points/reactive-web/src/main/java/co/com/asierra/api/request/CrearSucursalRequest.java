package co.com.asierra.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CrearSucursalRequest {

    private String nombre;

    private Integer franquiciaId;

    private Integer status;
}
