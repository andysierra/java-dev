package co.com.asierra.api;

import co.com.asierra.api.request.CrearFranquiciaRequest;
import co.com.asierra.api.request.CrearProductoRequest;
import co.com.asierra.api.request.CrearSucursalRequest;
import co.com.asierra.api.request.ModificarProductoRequest;
import co.com.asierra.model.franquicia.Franquicia;
import co.com.asierra.model.producto.Producto;
import co.com.asierra.model.sucursal.Sucursal;
import co.com.asierra.model.sucursalproducto.SucursalProducto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface Mappings {

    @Mapping(target = "id", ignore = true)
    Franquicia toFranquicia(CrearFranquiciaRequest request);

    @Mapping(target = "id", ignore = true)
    Sucursal toSucursal(CrearSucursalRequest request);

    @Mapping(target = "id", ignore = true)
    Producto toProducto(CrearProductoRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stock", ignore = true)
    @Mapping(target = "productoId", source = "producto.id")
    SucursalProducto toSucursalProducto(CrearProductoRequest request, Producto producto);

    @Mapping(target = "id", ignore = true)
    Producto toProductoMod(ModificarProductoRequest request);
}
