package tripong.backend.controller.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tripong.backend.config.security.authentication.token.CookieService;
import tripong.backend.config.security.principal.AuthDetail;
import tripong.backend.dto.account.AddressResponseDto;
import tripong.backend.dto.account.FirstExtraInfoPutRequestDto;
import tripong.backend.dto.account.NormalJoinRequestDto;
import tripong.backend.entity.user.AddressCategory;
import tripong.backend.exception.ErrorResult;
import tripong.backend.service.account.AccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AccountApiController {

    private final AccountService accountService;
    private final CookieService cookieService;

    /**
     * 일반 회원가입 API
     */
    @PostMapping("/auth/signup/normal")
    public ResponseEntity normalJoin(@Validated @RequestBody NormalJoinRequestDto dto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }
        accountService.normalJoin(dto, request, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 추가정보 입력 폼 요소들 반환 API
     */
    @GetMapping("/users/extra-info/form")
    public ResponseEntity firstExtraInfoPatchForm(){
        List<AddressResponseDto> result = Arrays.stream(AddressCategory.values()).map(a -> new AddressResponseDto(a.getCity(), a.getDistrict())).collect(Collectors.toList());
        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 추가정보 입력 API
     */
    @PatchMapping("/users/additional-info")
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
        response.addHeader("Set-Cookie", cookieService.refreshCookieExpired());
        return new ResponseEntity(HttpStatus.OK);
    }
}
