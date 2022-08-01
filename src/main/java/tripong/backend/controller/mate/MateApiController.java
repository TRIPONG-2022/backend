package tripong.backend.controller.mate;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tripong.backend.config.security.principal.AuthDetail;
import tripong.backend.dto.mate.MateRequestDto;
import tripong.backend.exception.ErrorResult;
import tripong.backend.service.mate.MateService;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MateApiController {

    private final MateService mateService;

    // 사용자 현재 위치 변경
    @PatchMapping("/users/mate/change/location")
    public ResponseEntity<Object> changeCurrentLocation(@Validated @RequestBody MateRequestDto dto, BindingResult bindingResult, @AuthenticationPrincipal AuthDetail principal) throws ParseException {

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }

        mateService.changeCurrentLocation(dto, principal);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 사용자와 동일한 메이트 매칭 리스트
    @GetMapping("/users/mate/match/{km}")
    public ResponseEntity<Object> getMateMatchingList(@Validated @RequestBody MateRequestDto dto, BindingResult bindingResult, @AuthenticationPrincipal AuthDetail principal, @PathVariable Double km){

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }

        Double currentLongitude = dto.getLongitude().doubleValue();
        Double currentLatitude = dto.getLatitude().doubleValue();

        List mateList = mateService.getMateMatchingList(currentLatitude, currentLongitude, principal, km);

        return new ResponseEntity<>(mateList, HttpStatus.OK);
    }




}
