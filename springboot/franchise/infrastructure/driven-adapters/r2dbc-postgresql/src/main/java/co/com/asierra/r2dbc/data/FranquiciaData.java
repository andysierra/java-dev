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
@Table("franquicia")
@AllArgsConstructor
@NoArgsConstructor
public class FranquiciaData {

    @Id
    private Integer id;

    @Column
    private String nombre;

    @Column
    private Integer status;
}
