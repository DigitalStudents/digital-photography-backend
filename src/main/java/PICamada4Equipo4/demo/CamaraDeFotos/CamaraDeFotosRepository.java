package PICamada4Equipo4.demo.CamaraDeFotos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CamaraDeFotosRepository extends JpaRepository<CamaraDeFotos, Long> {
}
