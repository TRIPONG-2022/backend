package tripong.backend.service.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tripong.backend.dto.authorization.FindPwAndIdRequestDto;
import tripong.backend.entity.user.User;
import tripong.backend.repository.authorization.UserAuthRepository;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserAuthRepository userAuthRepository;

    public Optional<User> findUserId (FindPwAndIdRequestDto dto) {
        return userAuthRepository.findByEmail(dto.getEmail());
    }

}
