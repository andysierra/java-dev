package co.com.asierra.usecase.franchise;

import co.com.asierra.model.franquicia.Franquicia;
import co.com.asierra.model.franquicia.gateways.FranquiciaRepository;
import co.com.asierra.model.producto.Producto;
import co.com.asierra.model.producto.gateways.ProductoRepository;
import co.com.asierra.model.sucursal.Sucursal;
import co.com.asierra.model.sucursal.gateways.SucursalRepository;
import co.com.asierra.model.sucursalproducto.SucursalProducto;
import co.com.asierra.model.sucursalproducto.gateways.SucursalProductoRepository;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase {

    private final FranquiciaRepository franquiciaRepository;
    private final SucursalRepository sucursalRepository;
    private final ProductoRepository productoRepository;
    private final SucursalProductoRepository sucursalProductoRepository;

    public Mono<Franquicia> crearFranquicia(Franquicia franquicia) {
        return franquiciaRepository.save(franquicia).onErrorResume(Mono::error);
    }

    public Mono<Sucursal> crearSucursal(Sucursal sucursal) {
        return franquiciaRepository.findById(sucursal.getFranquiciaId())
                .then(sucursalRepository.save(sucursal));
    }

    public Mono<Sucursal> buscarSucursalPorId(Integer id) {
        return sucursalRepository.findById(id);
    }

    public Mono<Map<String, String>> crearProducto(Producto producto, Sucursal sucursal) {
        return buscarProductoPorNombre(producto.getNombre())
                .switchIfEmpty(productoRepository.save(producto))
                .flatMap(foundProduct -> {
                    System.out.println("buscaré sucursal con datos: "+foundProduct.getId()+", "+sucursal.getId());
                    return sucursalProductoRepository.findOne(SucursalProducto.builder()
                            .productoId(foundProduct.getId())
                            .sucursalId(sucursal.getId())
                            .build())
                            .switchIfEmpty(sucursalProductoRepository.save(SucursalProducto.builder()
                                    .productoId(foundProduct.getId())
                                    .sucursalId(sucursal.getId())
                                    .stock(0L)
                                    .build()));
                })
                .flatMap(obtainedSucursalProducto -> {
                    obtainedSucursalProducto.setStock(obtainedSucursalProducto.getStock()+1);
                    return sucursalProductoRepository.save(obtainedSucursalProducto);
                })
                .flatMap(updatedSucursalProducto -> {
                    System.out.println("found sucursal: "+updatedSucursalProducto);
                    return Mono.just(Map.of(
                            "status", "producto añadido a sucursal",
                            "sucursal", updatedSucursalProducto.getSucursalId().toString(),
                            "producto", updatedSucursalProducto.getProductoId().toString()
                    ));
                });
    }

    public Mono<Map<String, String>> eliminarProductoDeSucursal(int sucursalId, int productoId) {
        SucursalProducto victima = SucursalProducto.builder()
                .productoId(productoId)
                .sucursalId(sucursalId)
                .build();

        return sucursalProductoRepository.findOne(victima)
                .flatMap(sucursalProducto -> {
                    System.out.println("FOUND: "+sucursalProducto);
                    sucursalProducto.setStock(sucursalProducto.getStock()-1);
                    return sucursalProductoRepository.save(sucursalProducto);
                })
                .flatMap(sucursalProducto -> Mono.just(Map.of(
                        "sucursal",sucursalProducto.getSucursalId().toString(),
                        "producto",sucursalProducto.getProductoId().toString()
                )))
                .switchIfEmpty(Mono.just(Map.of(
                        "status", "producto o sucursal no encontrados",
                        "sucursal", String.valueOf(sucursalId),
                        "producto", String.valueOf(productoId)
                )));

    }

    public Mono<Producto> buscarProductoPorNombre(String nombre) {
        return productoRepository.findOne(Producto.builder().nombre(nombre).build());
    }

    public Mono<Map<String, String>> modificarStock(int sucursalId, int productoId, Long nuevoStock) {
        SucursalProducto victima = SucursalProducto.builder()
                .productoId(productoId)
                .sucursalId(sucursalId)
                .build();

        return sucursalProductoRepository.findOne(victima)
                .flatMap(sucursalProducto -> {
                    System.out.println("FOUND: "+sucursalProducto);
                    sucursalProducto.setStock(nuevoStock);
                    return sucursalProductoRepository.save(sucursalProducto);
                })
                .flatMap(sucursalProducto -> Mono.just(Map.of(
                        "message","stock modificado",
                        "sucursal",sucursalProducto.getSucursalId().toString(),
                        "producto",sucursalProducto.getProductoId().toString(),
                        "nuevoStock",sucursalProducto.getStock().toString()
                )))
                .switchIfEmpty(Mono.just(Map.of(
                        "status", "producto o sucursal no encontrados",
                        "sucursal", String.valueOf(sucursalId),
                        "producto", String.valueOf(productoId)
                )));

    }

    public Mono<Map<String, String>> topPerSucursal(int franquiciaId) {
        return franquiciaRepository.findById(franquiciaId)
                .flatMapMany(franquicia ->
                        sucursalRepository.findByExample(Sucursal.builder().franquiciaId(franquiciaId).build()))
                .flatMap(sucursal
                        -> sucursalProductoRepository.findByExample(SucursalProducto.builder().sucursalId(sucursal.getId()).build()))
                .reduce((sp1, sp2) -> sp1.getStock() > sp2.getStock() ? sp1 : sp2)
                .flatMap(topStock -> productoRepository.findById(topStock.getProductoId())
                        .flatMap(producto -> Mono.just(Map.of(
                                "message","Producto Encontrado",
                                "producto",producto.toString(),
                                "sucursal",topStock.toString()
                        ))));
    }

    public Mono<Map<String, String>> modificarFranquicia(int id, Franquicia franquicia) {
        return franquiciaRepository.findById(id)
                .flatMap(found -> {
                    found.setNombre(franquicia.getNombre());
                    found.setStatus(franquicia.getStatus());
                    return franquiciaRepository.save(found);
                })
                .flatMap(saved -> Mono.just(Map.of(
                        "message","franquicia modificada",
                        "nombre",saved.getNombre(),
                        "status",saved.getStatus().toString()
                )))
                .switchIfEmpty(Mono.just(Map.of(
                        "message","franquicia no encontrada",
                        "nombre",franquicia.getNombre(),
                        "status",franquicia.getStatus().toString()
                )));
    }

    public Mono<Map<String, String>> modificarProducto(int id, Producto producto) {
        return productoRepository.findById(id)
                .flatMap(found -> {

                    System.out.println("INTENTANDO ACTUALIZAR PRODUCTO "+found.toString());
                    found.setNombre(producto.getNombre());
                    found.setStatus(producto.getStatus());
                    return productoRepository.save(found);
                })
                .flatMap(saved -> Mono.just(Map.of(
                        "message","producto modificado",
                        "nombre",saved.getNombre(),
                        "status",saved.getStatus().toString()
                )))
                .switchIfEmpty(Mono.just(Map.of(
                        "message","producto no encontrado",
                        "nombre",producto.getNombre(),
                        "status",producto.getStatus().toString()
                )));
    }
}
