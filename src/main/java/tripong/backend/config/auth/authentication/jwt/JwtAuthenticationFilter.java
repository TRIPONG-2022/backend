package tripong.backend.config.auth.authentication.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.config.auth.PrincipalDetail;
import tripong.backend.dto.account.NormalLoginRequestDto;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtCookieService cookieService;

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


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        normalLoginDto.getLoginId(),
                        normalLoginDto.getPassword());


        Authentication authentication =
                authenticationManager.authenticate(authenticationToken); //로그인안되면 401에러

        //성공 실패핸들러 추가예정
        log.info("종료: attemptAuthentication");
        return authentication;
    }


    //인증완료 후 실행
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.info("시작: successfulAuthentication");

        PrincipalDetail principal = (PrincipalDetail)authResult.getPrincipal();
        System.out.println("principal.getUsername(): " + principal.getUsername());
        System.out.println("principal.getUser(): " + principal.getUser());

        String jwtToken = JWT.create()
                .withSubject(principal.getUsername()) //토큰명
                .withExpiresAt(new Date(System.currentTimeMillis()+ (JwtProperties.EXPIRATION_TIME))) //만료시간 10분
                .withClaim("loginId", principal.getUser().getLoginId())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET)); //HMAC HS256에 쓰일 개인키

        response.addCookie(cookieService.jwtCookieIn(jwtToken));
        log.info("종료: successfulAuthentication");
    }
}



