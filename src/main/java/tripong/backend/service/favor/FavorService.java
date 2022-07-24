package tripong.backend.service.favor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.dto.favor.FavorRequestDto;
import tripong.backend.entity.favor.Favor;
import tripong.backend.entity.favor.TravelerType;
import tripong.backend.entity.user.User;
import tripong.backend.exception.favor.FavorErrorMessage;
import tripong.backend.repository.favor.FavorRepository;
import tripong.backend.repository.user.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavorService {

    private final FavorRepository favorRepository;
    private final UserRepository userRepository;

    // 여행가 타입 계산 및 저장
    @Transactional
    public Favor saveTravelerType(FavorRequestDto dto){

        List<Integer> answer = dto.getAnswer();
        int totalA = 0;
        int totalB = 0;

        for (int i = 0; i < answer.size() ; i++) {
            // 짝수 인덱스: 계획 점수
            if (i % 2 == 0){
                totalA = answer.get(i) + totalA;
            }
            // 홀수 인덱스: 관계 점수
            else{
                totalB = answer.get(i) + totalB;
            }
        }

        // 계획 점수와 관계 점수 각각 평균 값
        int scoreA = totalA / 6;
        int scoreB = totalB / 6;

        // 자유로운 인싸 여행가
        if (scoreA <= 2.5 && scoreB > 2.5){
            dto.setTravelerType(TravelerType.extravertedPerceiver);
        }
        // 자유로운 앗싸 여행가
        else if(scoreA <= 2.5 && scoreB <= 2.5){
            dto.setTravelerType(TravelerType.introvertedPerceiver);
        }
        // 계획형 인싸 여행가
        else if(scoreA > 2.5 && scoreB > 2.5){
            dto.setTravelerType(TravelerType.extravertedJudger);
        }
        // 계획형 앗싸 여행가
        else if(scoreA > 2.5 && scoreB <= 2.5){
            dto.setTravelerType(TravelerType.introvertedJudger);
        }

        Favor favor = dto.toEntity();

        Optional<User> user = userRepository.findByLoginId(dto.getUserId());
        favor.setUserId(user.orElseThrow(() -> new NoSuchElementException(FavorErrorMessage.User_NO_SUCH_ELEMENT)));

        return favorRepository.save(favor);
    }


}
