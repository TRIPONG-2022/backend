package tripong.backend.controller.authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tripong.backend.config.security.principal.AuthDetail;
import tripong.backend.dto.authentication.EmailAuthRequestDto;
import tripong.backend.exception.ErrorResult;
import tripong.backend.service.authentication.EmailAuthService;

import javax.mail.MessagingException;
import javax.validation.constraints.NotBlank;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmailAuthApiController {

    private final EmailAuthService emailAuthService;

    // 이메일 인증
    @GetMapping("/users/auth/send/email")
    public ResponseEntity<Object> sendEmailAuth(@Validated @RequestBody EmailAuthRequestDto dto, BindingResult bindingResult,  @AuthenticationPrincipal AuthDetail principal) throws MessagingException {

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }

        emailAuthService.createEmailValidLink(dto, principal);

        log.info("이메일 인증: " + principal.getLoginId() + " 유저 이메일 인증 승인");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 이메일 재인증
    @GetMapping("/users/auth/resend/email")
    public ResponseEntity<Object> resendEmailAuth(@Validated @RequestBody EmailAuthRequestDto dto, BindingResult bindingResult, @AuthenticationPrincipal AuthDetail principal) throws MessagingException {

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }

        emailAuthService.verifyResendEmailValidLink(dto, principal);

        log.info("이메일 인증: " + principal.getLoginId() + " 유저 이메일 재인증 승인");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 이메일 인증: URL 매핑
    @GetMapping("/users/auth/verify/email")
    public ResponseEntity<Object> emailConfirm(@Validated @NotBlank @RequestParam String emailValidLink, BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }

        emailAuthService.verifyEmailLink(emailValidLink);

        log.info("이메일 인증: " + emailValidLink + " 유효 링크 인증 완료");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
