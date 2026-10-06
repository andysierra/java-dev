package co.com.asierra.model.sucursalproducto;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SucursalProducto {

    private Integer id;

    private Integer sucursalId;

    private Integer productoId;

    private Long stock;
}
