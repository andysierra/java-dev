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
@Table("producto")
@AllArgsConstructor
@NoArgsConstructor
public class ProductoData {

    @Id
    private Integer id;

    @Column
    private String nombre;

    @Column
    private Integer status;
}
