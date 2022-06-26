package tripong.backend.config.auth.oauth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import tripong.backend.config.auth.PrincipalDetail;
import tripong.backend.config.auth.jwt.JwtProperties;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
@Component
public class CustomOauthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("시작: CustomOauthSuccessHandler");

        PrincipalDetail principal = (PrincipalDetail)authentication.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject(principal.getUsername()) //토큰명
                .withExpiresAt(new Date(System.currentTimeMillis()+ (JwtProperties.EXPIRATION_TIME))) //만료시간 10분
                .withClaim("loginId", principal.getUser().getLoginId())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET)); //HMAC HS256에 쓰일 개인키

        removeOauthCookies(request, response);


        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
        getRedirectStrategy().sendRedirect(request, response,"/");
        log.info("종료: CustomOauthSuccessHandler");
    }

    /**
     * 추후, front에서 들어오는 쿠키들 확인해서 삭제할 것
     */
    public void removeOauthCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals("쿠키삭제1") || cookie.getName().equals("쿠키삭제2") ) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }


}
