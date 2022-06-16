package tripong.backend.controller.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tripong.backend.dto.account.NormalJoinRequestDto;
import tripong.backend.service.account.AccountService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;


    @PostMapping("/users/signup/normal")
        public ResponseEntity normalJoin(@RequestBody NormalJoinRequestDto dto){
            System.out.println("dto = " + dto);
            accountService.normalJoin(dto);

            //성공 201,실패 400
            HttpStatus status = HttpStatus.CREATED;

        return new ResponseEntity<>(status);
    }




}
