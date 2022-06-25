package tripong.backend.controller.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tripong.backend.dto.authorization.FindPwAndIdRequestDto;
import tripong.backend.dto.authorization.FindPwAndIdResponseDto;
import tripong.backend.entity.user.User;
import tripong.backend.service.authorization.UserAuthService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthService userAuthService;

    // 아이디 찾기
    @PostMapping("/users/auth/id")
    public ResponseEntity findUserID (@RequestBody FindPwAndIdRequestDto dto){

        Optional<User> userOptional = userAuthService.findUserId(dto);
        User user;

        if (userOptional.isPresent()){
            user = userOptional.get();
            return new ResponseEntity<>(user, HttpStatus.FOUND);
        } else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }




}
