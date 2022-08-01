package tripong.backend.repository.favor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tripong.backend.entity.favor.Favor;
import tripong.backend.entity.favor.TravelerType;


public interface FavorRepository extends JpaRepository<Favor, Long> {

    @Query("SELECT f.travelerType FROM Favor f JOIN f.userId u where u.loginId = :loginId")
    TravelerType findByUserId(@Param(value = "loginId") String loginId);

}
