// infrastructure/driven-adapters/r2dbc-postgresql/src/main/java/co/com/asierra/r2dbc/data/SucursalProductoData.java
package co.com.asierra.r2dbc.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("sucursal_x_producto")
@AllArgsConstructor
@NoArgsConstructor
public class SucursalProductoData {

    @Id
    private Integer id;

    @Column
    private Integer sucursalId;

    @Column
    private Integer productoId;

    @Column
    private Long stock;
}