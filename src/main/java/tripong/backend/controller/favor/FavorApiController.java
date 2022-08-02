package tripong.backend.controller.favor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import tripong.backend.dto.favor.FavorRequestDto;
import tripong.backend.entity.favor.Favor;
import tripong.backend.exception.ErrorResult;
import tripong.backend.service.favor.FavorService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FavorApiController {

    private final FavorService favorService;

    // 여행가 타입 계산 및 저장
    @PostMapping("/users/favor/test")
    public ResponseEntity<Object> saveTravelerType(@Validated FavorRequestDto dto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }

        Favor favor = favorService.saveTravelerType(dto);

        log.info(dto.getUserId() + "의 여행가 타입은 " + favor.getTravelerType() + ": 테스트 완료" );
        return new ResponseEntity<>(favor.getTravelerType(), HttpStatus.OK);
    }


}
