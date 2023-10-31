package Backend.Producto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service


public class ProductoServiceImpl implements ProductoService {

    private List<Producto> shuffledProducts = null;

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
    public Page<Producto> Paginados(Pageable pageable) {
        // Check if the shuffledProducts list is empty, shuffle it once
        if (shuffledProducts == null) {
            shuffledProducts = productoRepository.findAll();
            Collections.shuffle(shuffledProducts);
        }

        // Calculate the start and end indices for the requested page
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int startIndex = pageNumber * pageSize;
        int endIndex = Math.min(startIndex + pageSize, shuffledProducts.size());

        // Sublist the shuffled products based on the page request
        List<Producto> productsOnPage = shuffledProducts.subList(startIndex, endIndex);

        // Create a Page containing the shuffled products
        return new PageImpl<>(productsOnPage, pageable, shuffledProducts.size());
    }



    @Override
    public void ModificarProducto(Producto producto) {
        productoRepository.save(producto);
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