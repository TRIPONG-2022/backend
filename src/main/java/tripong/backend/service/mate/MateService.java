package tripong.backend.service.mate;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.config.security.principal.AuthDetail;
import tripong.backend.dto.mate.MateRequestDto;
import tripong.backend.entity.user.User;
import tripong.backend.exception.mate.MateErrorMessage;
import tripong.backend.repository.favor.FavorRepository;
import tripong.backend.repository.user.UserRepository;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MateService {

    private final UserRepository userRepository;
    private final FavorRepository favorRepository;
    private final EntityManager entityManager;

    // 사용자 현재 위치 변경
    @Transactional
    public void changeCurrentLocation(MateRequestDto dto, AuthDetail principal) throws ParseException {

        User user = userRepository.findByLoginId(principal.getLoginId()).orElseThrow(() -> new NoSuchElementException(MateErrorMessage.User_NO_SUCH_ELEMENT));
        user.changeCurrentLocation(dto.getLongitude(), dto.getLatitude());

    }

    // 사용자와 동일한 메이트 매칭 리스트
    @Transactional
    public List getMateMatchingList(Double currentLatitude, Double currentLongitude, AuthDetail principal, Double km){

        String travel = String.valueOf(favorRepository.findByUserId(principal.getLoginId()));

        Query query = entityManager.createNativeQuery("SELECT u.id, u.login_id, u.name, u.nick_name, u.picture"
        + ", u.gender, u.introduction, u.city, u.district, u.traveler_type"
        + ", YEAR(NOW()) - LEFT(u.birth_date, 4) - 1 AS korean_age"
        + ", (6371*acos(cos(radians(:currentLatitude))*cos(radians(u.latitude))*cos(radians(u.longitude) -radians(:currentLongitude))+sin(radians(:currentLatitude))*sin(radians(u.latitude)))) AS distance "
        + "FROM User u "
        + "WHERE u.traveler_type = :travel "
        + "HAVING distance <= :km");

        query.setParameter("km", km);
        query.setParameter("travel", travel);
        query.setParameter("currentLatitude", currentLatitude);
        query.setParameter("currentLongitude", currentLongitude);

        List mateList = query.getResultList();

        return mateList;
    }

}
