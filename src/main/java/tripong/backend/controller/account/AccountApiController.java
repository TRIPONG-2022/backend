package tripong.backend.controller.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tripong.backend.config.security.authentication.token.CookieService;
import tripong.backend.config.security.principal.AuthDetail;
import tripong.backend.dto.account.FirstExtraInfoPutRequestDto;
import tripong.backend.dto.account.NormalJoinRequestDto;
import tripong.backend.exception.ErrorResult;
import tripong.backend.service.account.AccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AccountApiController {

    private final AccountService accountService;
    private final CookieService cookieService;

    /**
     * 일반 회원가입 API
     */
    @PostMapping("/users/signup/normal")
    public ResponseEntity normalJoin(@Validated @RequestBody NormalJoinRequestDto dto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            System.out.println("bindingResult = " + bindingResult);
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }
        accountService.normalJoin(dto, request, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 추가정보 입력 API
     */
    @PatchMapping("/users/extra-info")
    public ResponseEntity firstExtraInfoPatch(@Validated @RequestBody FirstExtraInfoPutRequestDto dto, BindingResult bindingResult, @AuthenticationPrincipal AuthDetail principal, HttpServletRequest request, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }
        accountService.firstExtraInfoPatch(dto, principal, request, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 회원 탈퇴 API
     */
    @PatchMapping("/users/withdrawal")
    public ResponseEntity withdrawal(@AuthenticationPrincipal AuthDetail principal, HttpServletResponse response){
        accountService.withdrawal(principal);
        response.addCookie(cookieService.refreshCookieExpired());
        return new ResponseEntity(HttpStatus.OK);
    }
}
