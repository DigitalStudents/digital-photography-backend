package Backend.Producto;


import Backend.AWSS3Service;
import Backend.Caracteristicas.Caracteristica;
import Backend.Caracteristicas.CaracteristicaRepository;
import Backend.Categorias.Categoria;
import Backend.Categorias.CategoriaRepository;
import Backend.ProductRating.ProductRating;
import Backend.ProductRating.ProductRatingDTO;
import Backend.ProductRating.ProductRatingRepository;
import Backend.User.Crud.UserRepository;
import Backend.User.Model.UserEntity;
import Backend.exceptions.ConflictException;
import Backend.exceptions.ProductNotFoundException;
import Backend.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    private List<Producto> shuffledProducts = null;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductRatingRepository productRatingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AWSS3Service awsS3Service;

    private static final Logger logger = LogManager.getLogger(ProductoServiceImpl.class);

    @Override
    public void CrearProducto(Producto producto) {
        if (productoRepository.findByNombreIgnoreCase(producto.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un producto con el mismo nombre.");
        }
        productoRepository.save(producto);
    }

    @Override
    @Transactional
    public void uploadImages(Long productId, List<MultipartFile> imageFiles) throws IOException {
        Optional<Producto> optionalProducto = productoRepository.findById(productId);
        if (optionalProducto.isEmpty()) {
            throw new IllegalArgumentException("Producto con ID " + productId + " no existe.");
        }

        Producto producto = optionalProducto.get();

        try {
            List<String> newImageUrls = awsS3Service.uploadImagesToS3(imageFiles);

            logger.info("Received new image URLs: {}", Arrays.toString(newImageUrls.toArray()));

            List<String> existingImageUrls = producto.getImagenes();
            existingImageUrls.addAll(newImageUrls);

            producto.setImagenes(existingImageUrls);

            productoRepository.save(producto);

            logger.info("New images associated with Producto (ID: {}) uploaded successfully.", productId);
        } catch (IOException e) {
            logger.error("Error uploading new images for Producto (ID: {}): {}", productId, e.getMessage(), e);
            throw e;
        }
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
        List<Producto> allProducts = productoRepository.findAll();

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int startIndex = pageNumber * pageSize;
        int endIndex = Math.min(startIndex + pageSize, allProducts.size());

        List<Producto> productsOnPage = allProducts.subList(startIndex, endIndex);
        Collections.shuffle(productsOnPage);

        return new PageImpl<>(productsOnPage, pageable, allProducts.size());
    }

    @Override
    public Page<Producto> BuscarPorNombre(String searchTerm, Pageable pageable) {
        return productoRepository.findByNombreContainingIgnoreCase(searchTerm, pageable);
    }

    @Override
    public List<Producto> filterProductosByCategorias(List<String> categoriaNombres) {
        return productoRepository.findByCategorias_NombreIn(categoriaNombres);
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

    @Override
    public void agregarCaracteristicasAProducto(Long productoId, List<Long> caracteristicaIds) {
        Optional<Producto> optionalProducto = productoRepository.findById(productoId);
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();

            List<Caracteristica> caracteristicas = caracteristicaRepository.findAllById(caracteristicaIds);
            
            producto.getCaracteristicas().addAll(caracteristicas);
            productoRepository.save(producto);
        }
    }

    @Override
    public void agregarCategoriasAProducto(Long productoId, List<Long> categoriaIds) {
        Optional<Producto> optionalProducto = productoRepository.findById(productoId);
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();

            List<Categoria> categorias = categoriaRepository.findAllById(categoriaIds);

            producto.getCategorias().addAll(categorias);
            productoRepository.save(producto);
        }
    }

    @Override
    public void addRating(Long productId, Long userId, int rating, String comment) {
        Producto product = productoRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado"));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        if (productRatingRepository.existsByProductAndUser(product, user)) {
            throw new ConflictException("Ya has valorado este producto.");
        }

        ProductRating productRating = new ProductRating();
        productRating.setProduct(product);
        productRating.setUser(user);
        productRating.setRating(rating);
        productRating.setComment(comment);
        productRating.setDate(LocalDateTime.now());

        productRatingRepository.save(productRating);
    }

    @Override
    public double getAverageRating(Long productId) {
        List<ProductRating> ratings = productRatingRepository.findByProduct_Id(productId);

        if (ratings.isEmpty()) {
            return 0;
        }

        double sum = ratings.stream().mapToDouble(ProductRating::getRating).sum();
        return sum / ratings.size();
    }

    @Override
    public List<ProductRatingDTO> getProductRatings(Long productId) {
        List<ProductRating> ratings = productRatingRepository.findByProduct_Id(productId);

        return ratings.stream()
                .map(ProductRatingDTO::new)
                .collect(Collectors.toList());
    }

}
