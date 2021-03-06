package tripong.backend.config.security.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.dto.admin.resource.AccessDeniedReasonDto;
import tripong.backend.dto.admin.resource.ResourceRolesDto;
import tripong.backend.entity.role.Resource;
import tripong.backend.entity.role.RoleResource;
import tripong.backend.exception.ErrorResult;
import tripong.backend.service.admin.ResourceService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private HttpSecurity httpSecurity;
    public CustomAccessDeniedHandler(HttpSecurity httpSecurity) {
        this.httpSecurity = httpSecurity;
    }
    @Autowired private ResourceService resourceService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> user_authorities = authentication.getAuthorities();

        StringBuilder role = new StringBuilder();
        List<AccessDeniedReasonDto> accessReason = resourceService.findAccessReason();
        accessReason.stream().forEach( a -> {
            AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher(a.getResourceName());
            if (antPathRequestMatcher.matches(request)) {
                List<ResourceRolesDto> roleResources = a.getRoleResources();
                roleResources.forEach( r -> {
                    role.append(r.getRoleName().substring(5) + " ");
                });
            }
        });

        String message = "????????? ????????? ???????????????.";
        int code = 900;
        HttpStatus status = HttpStatus.FORBIDDEN;

        if(user_authorities.toString().contains("BLACK")){
            message = "???????????? ?????? ?????????. ????????? ????????? ???????????????.";
            code = 901;
        }
        else if(!(user_authorities.toString().contains("ROLE_USER"))){
            message = "???????????? ?????? ??? ?????? ???????????????.";
            code = 903;
        }
        else if(role.toString().contains("ADMIN")){
            message = "???????????? ?????? ???????????????.";
            code = 902;
        }
        else{
            message = "????????? ????????? ???????????????." + role;
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(403);
        response.setCharacterEncoding("utf-8");
        ResponseEntity<ErrorResult> error = new ResponseEntity<>(new ErrorResult(code, message), status);
        response.getWriter().write(objectMapper.writeValueAsString(error.getBody()));
    }
}
