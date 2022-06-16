package tripong.backend.service.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.entity.user.User;
import tripong.backend.entity.user.JoinType;
import tripong.backend.entity.user.RoleType;
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
        dto.setPassword(encoder.encode(dto.getPassword()));
        dto.setRole(RoleType.User);
        dto.setJoinMethod(JoinType.Normal);
        dto.setAuthentication(0);
        User user = dto.toEntity();

        userRepository.save(user);
        System.out.println("종료: 회원가입서비스");
    }

}
