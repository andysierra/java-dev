package co.com.asierra.r2dbc.repository.sucursal;

import co.com.asierra.r2dbc.data.SucursalData;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

// TODO: This file is just an example, you should delete or modify it
public interface SucursalReactiveRepository extends ReactiveCrudRepository<SucursalData, Integer>, ReactiveQueryByExampleExecutor<SucursalData> {

}
