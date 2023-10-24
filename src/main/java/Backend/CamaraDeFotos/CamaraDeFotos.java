package Backend.CamaraDeFotos;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;


@Data
@Entity
@DynamicUpdate
@NoArgsConstructor

@Table(name = "camaras")
public class CamaraDeFotos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String modelo;
    private String marca;
    private String tipoSensor;
    private String tipoLente;
    private String rutaImagen;
    private int resolucion;
    private double precio;
    private double aperturaMaxima;

    @PrePersist
    @PreUpdate
    private void validateData() {
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser positivo.");
        }
        if (resolucion < 0) {
            throw new IllegalArgumentException("La resolución no debe ser negativa.");
        }
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("Modelo no puede estar vacío");
        }
        if (resolucion == 0) {
            throw new IllegalArgumentException("Modelo no puede estar vacío");
        }
    }
}
