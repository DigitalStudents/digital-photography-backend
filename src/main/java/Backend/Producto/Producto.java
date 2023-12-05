package Backend.Producto;


import Backend.Caracteristicas.Caracteristica;
import Backend.Categorias.Categoria;
import Backend.Inventory.Inventory;
import Backend.ProductRating.ProductRating;
import Backend.Reservation.Reservation;
import Backend.User.Model.UserEntity;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;


@Data
@Entity
@DynamicUpdate
@NoArgsConstructor

@Table(name = "productos")
public class Producto{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;
    private double precio;
    private String descripcion;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "producto_imagenes", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "imagen_url")
    private List<String> imagenes;

    @ManyToMany
    @JoinTable(
            name = "producto_caracteristicas",
            joinColumns = @JoinColumn(name = "producto_id"),
            inverseJoinColumns = @JoinColumn(name = "caracteristica_id")
    )
    private List<Caracteristica> caracteristicas;

    @ManyToMany
    @JoinTable(
            name = "producto_categorias",
            joinColumns = @JoinColumn(name = "producto_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id"))
    private List<Categoria> categorias;

    @ManyToMany(mappedBy = "favoriteProducts")
    @JsonIgnore
    private List<UserEntity> favoritedByUsers = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "producto")
    private Inventory inventory;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductRating> ratings = new ArrayList<>();

    private boolean deleted = false;

    private static final String S3_BUCKET_NAME ="1023c04-grupo4";

    @Value("${myAccessKey}")
    private static String accessKey;
    @Value("${mySecretKey}")
    private static String secretKey;


    public void uploadImagesToS3(List<MultipartFile> imageFiles) throws IOException {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,secretKey);
        AmazonS3 S3_CLIENT = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.US_EAST_2)
                .build();
        for (MultipartFile imageFile : imageFiles) {
            String uniqueImageName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(imageFile.getSize());

            String contentType = getContentTypeByFileExtension(imageFile.getOriginalFilename());
            metadata.setContentType(contentType);

            S3_CLIENT.putObject(S3_BUCKET_NAME, uniqueImageName, new ByteArrayInputStream(imageFile.getBytes()), metadata);

            String imageUrl = generateS3ImageUrl(uniqueImageName);

            imagenes.add(imageUrl);
        }
    }

    private String generateS3ImageUrl(String imageName) {
        return "https://" + S3_BUCKET_NAME + ".s3.amazonaws.com/" + imageName;
    }


    private String getContentTypeByFileExtension(String filename) {
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filename.endsWith(".png")) {
            return "image/png";
        } else if (filename.endsWith(".gif")) {
            return "image/gif";
        } else {
            return "application/octet-stream";
        }
    }

    @JsonIgnore
    public void softDelete() {
        this.deleted = true;
    }

    public void restore() {
        this.deleted = false;
    }


}
