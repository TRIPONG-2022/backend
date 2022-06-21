package tripong.backend.service.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.entity.user.User;
import tripong.backend.dto.account.NormalJoinRequestDto;
import tripong.backend.repository.user.UserRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public void normalJoin(NormalJoinRequestDto dto){
        log.info("시작: AccountService 일반회원가입");

        boolean loginId_dup = userRepository.existsByLoginId(dto.getLoginId());
        boolean nickName_dub = userRepository.existsByNickName(dto.getNickName());

        if(loginId_dup && nickName_dub){
            throw new IllegalStateException("아이디&닉네임 중복");
        }
        if(loginId_dup){
            throw new IllegalStateException("아이디 중복");
        }
        if(nickName_dub){
            throw new IllegalStateException("닉네임 중복");
        }

        dto.setPassword(encoder.encode(dto.getPassword()));
        User user = dto.toEntity();
        userRepository.save(user);
        log.info("종료: AccountService 일반회원가입");
    }

}
