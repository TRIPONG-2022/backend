package tripong.backend.config.auth.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import tripong.backend.config.auth.jwt.JwtProperties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutHandler implements LogoutHandler {
    /**
     * 로그아웃 필터
     * -쿠키 초기화
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Cookie cookie = new Cookie(JwtProperties.HEADER_STRING, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
