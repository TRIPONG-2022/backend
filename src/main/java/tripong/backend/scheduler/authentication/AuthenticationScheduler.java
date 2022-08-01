package tripong.backend.scheduler.authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import tripong.backend.service.authentication.EmailAuthService;

import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationScheduler {

    private final EmailAuthService emailAuthService;

    @Scheduled(cron = "0 00 00 * * *")
    @DeleteMapping("/users/auth/delete/link")
    public void deleteValidLink(){
        emailAuthService.deleteValidLink();
        log.info(new Date() + "유효 링크 삭제 스케줄러 실행");
    }



}
