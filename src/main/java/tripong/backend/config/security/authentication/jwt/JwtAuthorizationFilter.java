package tripong.backend.config.security.authentication.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.config.security.principal.PrincipalDetail;
import tripong.backend.entity.user.User;
import tripong.backend.repository.user.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Transactional(readOnly = true)
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository){
        super(authenticationManager);
        this.userRepository=userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("시작: JwtAuthorizationFilter");

        Cookie[] cookies = request.getCookies();
        Cookie auth_cookie = null;
        if(cookies!=null){
            for(Cookie cookie: cookies){
                if(cookie.getName().equals("Authorization")){
                    auth_cookie = cookie;
                }
            }
        }
        if(auth_cookie==null) {
            log.info("종료: JwtAuthorizationFilter - 미로그인자의 요청");
            chain.doFilter(request, response);
            return;
        }
        String token = auth_cookie.getValue();
        String loginId = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                .getClaim("loginId").asString(); //만료에러 고려


        if(loginId != null){
            User user = userRepository.findPrincipleServiceByLoginId(loginId)
                    .orElseThrow(()->{
                        return new UsernameNotFoundException("잘못된 토큰 정보 입니다.");
                    });
            PrincipalDetail principal = new PrincipalDetail(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        log.info("종료: JwtAuthorizationFilter - 인가 ok");
        chain.doFilter(request, response);
    }
}
