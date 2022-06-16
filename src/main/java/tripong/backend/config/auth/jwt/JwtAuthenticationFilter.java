package tripong.backend.config.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tripong.backend.config.auth.CustomAuthenticationManager;
import tripong.backend.dto.account.NormalLoginRequestDto;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final CustomAuthenticationManager authenticationManager;

    @Override
    public void setUsernameParameter(String usernameParameter) {
        super.setUsernameParameter("loginId");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {


        log.info("시작: attemptAuthentication");
        ObjectMapper om = new ObjectMapper();
        NormalLoginRequestDto normalLoginDto = null;
        try {
            normalLoginDto = om.readValue(request.getInputStream(), NormalLoginRequestDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info(normalLoginDto.toString());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        normalLoginDto.getLoginId(),
                        normalLoginDto.getPassword());


        Authentication authentication =
                authenticationManager.authenticate(authenticationToken); //로그인안되면 401에러
        //성공시 홀더에 삽입(권한처리 용이)


        //성공 실패핸들러 추가예정
        log.info("종료: attemptAuthentication");
        return authentication;
    }


    //인증완료 후 실행
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.info("시작: successfulAuthentication");

        String loginId = authResult.getPrincipal().toString();
        System.out.println("loginId = " + loginId);

        String jwtToken = JWT.create()
                .withSubject("JWD_INFO") //토큰명
                .withExpiresAt(new Date(System.currentTimeMillis()+ (JwtProperties.EXPIRATION_TIME))) //만료시간 10분
                .withClaim("username", loginId)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET)); //HMAC HS256에 쓰일 개인키
        log.info("종료: successfulAuthentication");
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken); //헤더 담아 응답
    }
}
