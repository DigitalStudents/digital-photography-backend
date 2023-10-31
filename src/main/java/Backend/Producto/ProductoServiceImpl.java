package Backend.Producto;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service


public class ProductoServiceImpl implements ProductoService {

    private List<Producto> shuffledProducts = null;
    private Producto producto;

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

        if (shuffledProducts == null) {
            shuffledProducts = productoRepository.findAll();
            Collections.shuffle(shuffledProducts);
        }


        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int startIndex = pageNumber * pageSize;
        int endIndex = Math.min(startIndex + pageSize, shuffledProducts.size());


        List<Producto> productsOnPage = shuffledProducts.subList(startIndex, endIndex);


        return new PageImpl<>(productsOnPage, pageable, shuffledProducts.size());
    }

    @Override
    public void subirImagen(String imageUrl, byte[] imageBytes) {
        producto.addImageToBucket(imageUrl, imageBytes);
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