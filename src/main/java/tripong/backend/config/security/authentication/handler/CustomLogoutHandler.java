package tripong.backend.config.security.authentication.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import tripong.backend.config.security.authentication.token.CookieService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final CookieService cookieService;
    private final RedisTemplate redisTemplate;

    /**
     * 로그아웃 필터
     * -쿠키 초기화
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        response.addCookie(cookieService.refreshCookieExpired());
        redisTemplate.delete("RoleUpdate:"+authentication.getName());
    }
}
