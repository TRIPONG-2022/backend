package tripong.backend.controller.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tripong.backend.config.security.principal.PrincipalDetail;
import tripong.backend.dto.authentication.EmailAuthRequestDto;
import tripong.backend.exception.ErrorResult;
import tripong.backend.service.authentication.EmailAuthService;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class EmailAuthApiController {

    private final EmailAuthService emailAuthService;

    // 이메일 인증
    @GetMapping("/users/auth/email/send")
    public ResponseEntity<Object> sendEmailAuth(@Valid @RequestBody EmailAuthRequestDto dto, @AuthenticationPrincipal PrincipalDetail principal, BindingResult bindingResult) throws MessagingException {

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }

        emailAuthService.createEmailValidLink(dto, principal);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 이메일 재인증
    @GetMapping("/users/auth/email/resend")
    public ResponseEntity<Object> resendEmailAuth(@Valid @RequestBody EmailAuthRequestDto dto, @AuthenticationPrincipal PrincipalDetail principal, BindingResult bindingResult) throws MessagingException {

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }

        dto.setUserId(principal.getUser().getLoginId());
        emailAuthService.verifyResendEmailValidLink(dto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 이메일 인증: URL 매핑
    @GetMapping("/users/auth/email/confirm")
    public ResponseEntity<Object> emailConfirm(@Valid @NotBlank @RequestParam String emailValidLink, BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }

        emailAuthService.verifyEmailLink(emailValidLink);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
