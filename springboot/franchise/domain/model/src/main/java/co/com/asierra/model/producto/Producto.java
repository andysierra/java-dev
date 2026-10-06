package co.com.asierra.model.producto;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Producto {

    private Integer id;

    private String nombre;

    private Integer status;
}
