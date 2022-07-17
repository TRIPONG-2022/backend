package tripong.backend.controller.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tripong.backend.config.security.authentication.jwt.JwtCookieService;
import tripong.backend.config.security.principal.PrincipalDetail;
import tripong.backend.dto.account.FirstExtraInfoPutRequestDto;
import tripong.backend.dto.account.NormalJoinRequestDto;
import tripong.backend.exception.ErrorResult;
import tripong.backend.service.account.AccountService;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AccountApiController {

    private final AccountService accountService;
    private final JwtCookieService cookieService;

    /**
     * 일반 회원가입 API
     */
    @PostMapping("/users/signup/normal")
    public ResponseEntity normalJoin(@Validated @RequestBody NormalJoinRequestDto dto, BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }
        accountService.normalJoin(dto, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 추가정보 입력 API
     */
    @PatchMapping("/users/extra-info")
    public ResponseEntity firstExtraInfoPatch(@Validated @RequestBody FirstExtraInfoPutRequestDto dto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetail principal){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }
        accountService.firstExtraInfoPatch(dto, principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 회원 탈퇴 API
     */
    @PatchMapping("/users/withdrawal")
    public ResponseEntity withdrawal(@AuthenticationPrincipal PrincipalDetail principal, HttpServletResponse response){
        accountService.withdrawal(principal);
        response.addCookie(cookieService.jwtCookieExpired());
        return new ResponseEntity(HttpStatus.OK);
    }
}
