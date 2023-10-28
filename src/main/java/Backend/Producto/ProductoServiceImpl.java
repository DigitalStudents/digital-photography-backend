package Backend.Producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public void CrearProducto(Producto producto) {
        productoRepository.save(producto);
    }

    @Override
    public Optional<Producto> BuscarProducto(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public List<Producto> TraerTodos() {
        return productoRepository.findAll();
    }

    @Override
    public void ModificarProducto(Producto producto) {
        productoRepository.save(producto);
    }

    @Override
    public List<Producto> obtenerProductosAleatorios(int cantidad) {
        List<Producto> allProducts = productoRepository.findAll();

        // Shuffle the list of all products to make it random
        Collections.shuffle(allProducts);

        if (cantidad >= allProducts.size()) {
            return allProducts;
        } else {
            return allProducts.subList(0, cantidad);
        }
    }

    @Override
    public void EliminarProducto(Long id) {
        Optional<Producto> productoOptional = productoRepository.findById(id);
        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get();
            producto.softDelete();
            productoRepository.save(producto);
        }
    }

    @Override
    public void RestaurarProducto(Long id) {
        Optional<Producto> productoOptional = productoRepository.findById(id);
        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get();
            producto.restore();
            productoRepository.save(producto);
        }
    }
}