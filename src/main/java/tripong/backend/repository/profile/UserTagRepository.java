package tripong.backend.repository.profile;

import org.springframework.data.jpa.repository.JpaRepository;
import tripong.backend.entity.tag.Tag;

public interface UserTagRepository extends JpaRepository<Tag, Long> {
}
