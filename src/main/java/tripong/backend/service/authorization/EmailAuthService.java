package tripong.backend.service.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.dto.authorization.EmailAuthRequestDto;
import tripong.backend.entity.authorization.EmailValidLink;
import tripong.backend.repository.authorization.EmailAuthRepository;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

    private final JavaMailSender mailSender;
    private final EmailAuthRepository emailAuthRepository;

    // 이메일 유효링크 인증
    public String createEmailValidLik(EmailAuthRequestDto earDto) {

        // 이메일 유효링크 저장
        EmailValidLink validLink = EmailValidLink.createEmailValidLink(earDto.getUserId());
        emailAuthRepository.save(validLink);

        //이메일 발송 서비스 호출
        sendEmail(earDto, validLink);

        // 이메일 전송 시 토큰 반환
        return validLink.getId();
    }

    // 비동기식 이메일 발송
    @Async
    public void sendEmail(EmailAuthRequestDto earDto, EmailValidLink validLink){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(earDto.getUserId());
        message.setSubject("회원가입 이메일 인증");
        message.setText("http://tripong.com/confirm-email?validLink=" + validLink.getId());

        mailSender.send(message);
    }

    // 유효한 토큰 확인
    public EmailValidLink findByIdAndExpirationDateAfterAndExpired(String emailValidLink) {

        // 정상
        Optional<EmailValidLink> validLink  = emailAuthRepository.findByIdAndExpirationDateAfterAndExpired(emailValidLink, LocalDateTime.now(), false);

        // 실패: 토큰이 없다면 예외 발생
        return validLink.orElseThrow(()  -> new NoSuchElementException("valid link not found"));
    }


}
