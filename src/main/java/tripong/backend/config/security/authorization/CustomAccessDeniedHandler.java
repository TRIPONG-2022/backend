package tripong.backend.config.security.authorization;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        System.out.println("accessDeniedException = " + accessDeniedException);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        System.out.println("authorities = " + authorities.toString());
        if(authorities.toString().contains("BLACK")){
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "블랙 유저");
        }
        else if(authorities.toString().endsWith("UNAUTH]")){
            response.sendRedirect("/auth/additional-info");
        }
        else{
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근금지");
        }
    }


}
