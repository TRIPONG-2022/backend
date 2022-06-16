package tripong.backend.config.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private final PrincipalService principalService;
    private final BCryptPasswordEncoder encoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        log.info("시작: authenticate");
        PrincipalDetail detail = (PrincipalDetail) principalService.loadUserByUsername(authentication.getName());
        if (!encoder.matches(authentication.getCredentials().toString(), detail.getPassword())) {
            throw new BadCredentialsException("비밀번호 오류");
        }
        log.info("종료: authenticate");
        return new UsernamePasswordAuthenticationToken(detail.getUsername(), detail.getPassword(), detail.getAuthorities());
    }
}
