package tripong.backend.config.auth.oauth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tripong.backend.config.auth.PrincipalDetail;
import tripong.backend.config.auth.jwt.JwtProperties;

import javax.servlet.ServletException;
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


        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        log.info("종료: CustomOauthSuccessHandler");
    }
}
