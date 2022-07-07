package java.tripong.backend.controller.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tripong.backend.config.auth.PrincipalDetail;
import tripong.backend.dto.account.FirstExtraInfoPutRequestDto;
import tripong.backend.dto.account.NormalJoinRequestDto;
import tripong.backend.service.account.AccountService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AccountController {

    private final AccountService accountService;


    /**
     * 일반 회원가입 API
     */
    @PostMapping("/users/signup/normal")
    public ResponseEntity normalJoin(@Validated @RequestBody NormalJoinRequestDto dto){
        log.info("시작: AccountController 회원가입" + dto);
        accountService.normalJoin(dto);


        //성공 201,실패 400
        HttpStatus status = HttpStatus.CREATED;
        log.info("종료: AccountController 회원가입");
        return new ResponseEntity<>(status);
    }

    /**
     * 추가정보 입력 API
     */
    @PatchMapping("/users/extra-info")
    public ResponseEntity firstExtraInfoPatch(@Validated @RequestBody FirstExtraInfoPutRequestDto dto, @AuthenticationPrincipal PrincipalDetail principal){
        log.info("시작: AccountController 추가정보입력");
        accountService.firstExtraInfoPatch(dto, principal);

        log.info("종료: AccountController 추가정보입력");
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(status);
    }

}
