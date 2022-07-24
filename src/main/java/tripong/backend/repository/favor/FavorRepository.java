package tripong.backend.repository.favor;

import org.springframework.data.jpa.repository.JpaRepository;
import tripong.backend.entity.favor.Favor;

public interface FavorRepository extends JpaRepository<Favor, Long> {
}
