package tripong.backend.service.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.config.security.principal.PrincipalDetail;
import tripong.backend.dto.authentication.EmailAuthRequestDto;
import tripong.backend.dto.authentication.PasswordRequestDto;
import tripong.backend.entity.authentication.EmailValidLink;
import tripong.backend.entity.user.User;
import tripong.backend.exception.authentication.AuthenticationErrorMessage;
import tripong.backend.repository.authentication.EmailAuthRepository;
import tripong.backend.repository.authentication.UserAuthRepository;
import tripong.backend.repository.user.UserRepository;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserAuthService {

    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final EmailAuthService emailAuthService;

    // 아이디 찾기
    @Transactional
    public Optional<User> findUserId (EmailAuthRequestDto dto) {

        return userAuthRepository.findByEmail(dto.getEmail());

    }

    // 비밀번호 찾기: 이메일 인증
    @Transactional
    public void findUserPassword(EmailAuthRequestDto dto) throws MessagingException {

        String userId = String.valueOf(userAuthRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new NoSuchElementException(AuthenticationErrorMessage.User_NO_SUCH_ELEMENT)));

        EmailValidLink validLink = EmailValidLink.createEmailValidLink(userId);
        emailAuthRepository.save(validLink);

        sendFindUserPasswordByGmail(dto, validLink);

    }

    // 비밀번호 찾기: 이메일 재인증
    @Transactional
    public void verifyResendfindUserPassword(EmailAuthRequestDto dto) throws MessagingException {

        String userId = String.valueOf(userAuthRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new NoSuchElementException(AuthenticationErrorMessage.User_NO_SUCH_ELEMENT)));

        EmailValidLink emailValidLink = emailAuthRepository.findByTheLatestEmailToken(userId).orElseThrow(() -> new  IllegalArgumentException(AuthenticationErrorMessage.Email_Valid_Link_NO_SUCH_ELEMENT));

        if (emailValidLink.getCreatedTime().isBefore(LocalDateTime.now().minusMinutes(5))){
            EmailValidLink validLink = EmailValidLink.createEmailValidLink(userId);
            emailAuthRepository.save(validLink);
            sendFindUserPasswordByGmail(dto, validLink);
        } else {
            throw new IllegalStateException(AuthenticationErrorMessage.Resend_Email_Auth_FAIL);
        }


    }

    // 비밀번호 찾기: 비동기식 JavaMailSender
    @Async
    public void sendFindUserPasswordByGmail(EmailAuthRequestDto dto, EmailValidLink validLink) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        String text = "";
        String link = "http://localhost:8089/users/auth/find/password/view?emailValidLink=" + validLink.getId();
        // https://tripong-development.herokuapp.com

        message.setRecipients(Message.RecipientType.TO, dto.getEmail());
        message.setSubject("비밀번호 재설정 이메일");

        text += "<html><body>";
        text += "<div style = 'margin:100px; border-top: 3px solid #0DC5D6; border-bottom: 1px solid #7A7A7A; padding: 30px 0 30px 0;'>";
        text += "<br>";
        text += "<h1><span style='color: #0DC5D6;'>비밀번호 재설정</span> 안내입니다.</h1>";
        text += "<br>";
        text += "<p style='font-size: 14px'>여행 커뮤니티 서비스, <span style='color: #0DC5D6; font-weight: bold;'>Tripong</span>입니다. </p>";
        text += "<p style='font-size: 14px'>아래 <span style='color: #0DC5D6; font-weight: bold;'>링크</span>를 클릭해서 비밀번호를 재설정해주세요.</p>";
        text += "<p style='font-size: 11px'>비밀번호 재설정 링크는 메일이 발송된 시점부터 5분간만 유효합니다.</p>";
        text += "<br>";
        text += "<div align='center' style='background-color: #F7F7F7;padding: 40px;'>";
        text += link;
        text += "</div>";
        text += "<br>";
        text += "<p style='font-size: 14px'>감사합니다.</p>";
        text += "<br>";
        text += "</div>";
        text += "</body></html>";

        message.setText(text, "utf-8", "html");

        mailSender.send(message);

    }

    // 비밀번호 찾기: 유효 링크 확인인
   @Transactional
    public void verifyfindUserPasswordEmail(PasswordRequestDto dto){

        EmailValidLink findValidLink = emailAuthService.findByIdAndExpirationDateAfterAndExpired(dto.getValidLink());
        String userId = findValidLink.getUserId();

        findValidLink.makeInvalidLink();

        resetUserPassword(userId, dto);

    }

    // 비밀번호 찾기: 비밀번호 재설정
    @Transactional
    public void resetUserPassword(String userId, PasswordRequestDto dto){

        String newPassword = passwordEncoder.encode(dto.getNewPassword());

        User user = userRepository.findByLoginId(userId).orElseThrow(() -> new NoSuchElementException(AuthenticationErrorMessage.User_NO_SUCH_ELEMENT));
        user.changePassword(newPassword);

    }

    // 비밀번호 바꾸기
    @Transactional
    public void changeUserPassword(PasswordRequestDto dto, PrincipalDetail principal){

        String newPassword = passwordEncoder.encode(dto.getNewPassword());

        User user = userRepository.findByLoginId(principal.getUser().getLoginId()).orElseThrow(() -> new NoSuchElementException(AuthenticationErrorMessage.User_NO_SUCH_ELEMENT));
        user.changePassword(newPassword);

    }

}
