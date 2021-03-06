package tripong.backend.config.security.authentication.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import tripong.backend.exception.ErrorResult;
import tripong.backend.exception.account.AccountErrorMessage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String message = null;
        HttpStatus status = HttpStatus.OK;

        int code = 999;
        if(exception instanceof UsernameNotFoundException){
            message= AccountErrorMessage.LoginId_NOT_MATCH;
            code = 110;
        }
        else if(exception instanceof BadCredentialsException){
            message= AccountErrorMessage.Password_NOT_MATCH;
            code = 111;
        }
        else{
            message = "onAuthenticationFailure 미정의 에러";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        ResponseEntity<ErrorResult> error = new ResponseEntity<>(new ErrorResult(code, message), status);
        response.getWriter().write(objectMapper.writeValueAsString(error.getBody()));
    }
}
