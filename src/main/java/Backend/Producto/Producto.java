package Backend.Producto;



import Backend.Inventory.Inventory;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;


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
    private String descripcion;
    private double precio;

    @OneToOne(mappedBy = "producto")
    private Inventory inventory;
    private boolean deleted = false;

    public void softDelete() {
        this.deleted = true;
    }

    public void restore() {
        this.deleted = false;
    }


}
