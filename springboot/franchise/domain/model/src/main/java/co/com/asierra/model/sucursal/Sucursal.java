package co.com.asierra.model.sucursal;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Sucursal {

    private Integer id;

    private String nombre;

    private Integer franquiciaId;

    private Integer status;
}
