package Backend.Producto;



import Backend.Inventory.Inventory;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.io.ByteArrayInputStream;
import java.util.List;


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

    private static final String S3_BUCKET_NAME = "s3-bucket-nombre";
    private static final AmazonS3 S3_CLIENT = AmazonS3ClientBuilder.defaultClient();

    public void addImageToBucket(String imageUrl, byte[] imageBytes) {
        S3_CLIENT.putObject(S3_BUCKET_NAME, imageUrl, new ByteArrayInputStream(imageBytes), new ObjectMetadata());
    }

    public void removeImageFromBucket(String imageUrl) {
        S3_CLIENT.deleteObject(S3_BUCKET_NAME, imageUrl);
    }

    public void addImage(String imageUrl, byte[] imageBytes) {
        imagenes.add(imageUrl);
        addImageToBucket(imageUrl, imageBytes);
    }

    public void removeImage(String imageUrl) {
        imagenes.remove(imageUrl);
        removeImageFromBucket(imageUrl);
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
