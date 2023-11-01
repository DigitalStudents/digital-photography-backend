package Backend.Producto;



import Backend.Inventory.Inventory;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Data
@Entity
@DynamicUpdate
@NoArgsConstructor

@Table(name = "productos")
public class Producto{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String categoria;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_imagenes", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "imagen_url")
    private List<String> imagenes;
    @Column(unique = true)
    private String nombre;
    private String descripcion;
    private double precio;

    private static final String S3_BUCKET_NAME ="1023c04-grupo1";
    private static final AmazonS3 S3_CLIENT = AmazonS3ClientBuilder.standard()
            .withCredentials(new DefaultAWSCredentialsProviderChain())
            .withRegion(Regions.US_EAST_2)
            .build();

    public void uploadImageToS3(MultipartFile imageFile) throws IOException {
        String uniqueImageName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(imageFile.getSize());

        S3_CLIENT.putObject(S3_BUCKET_NAME, uniqueImageName, new ByteArrayInputStream(imageFile.getBytes()), metadata);

        imagenes.add(uniqueImageName);
    }


    @OneToOne(mappedBy = "producto")
    @JsonIgnore
    private Inventory inventory;
    private boolean deleted = false;

    public void softDelete() {
        this.deleted = true;
    }

    public void restore() {
        this.deleted = false;
    }


}
