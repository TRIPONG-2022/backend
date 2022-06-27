package tripong.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.entity.user.User;
import tripong.backend.entity.user.GenderType;
import tripong.backend.entity.user.JoinType;
import tripong.backend.entity.user.RoleType;
import tripong.backend.repository.user.UserRepository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.init1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;
        private final BCryptPasswordEncoder passwordEncoder;
        private final UserRepository userRepository;

        public void init1(){
            String pw = passwordEncoder.encode("1234");

            User user1 = User.builder() //일반 회원가입자 + 추가정보 미입력 + 이메일 미인증
                    .loginId("user1")
                    .password(pw)
                    .nickName("홍길동")
                    .email("abc11@naver.com")
                    .joinMethod(JoinType.Normal)
                    .authentication(0)
                    .role(RoleType.Unauth)
                    .build();
            User user2 = User.builder() //일반 회원가입 + 추가정보 입력 + 이메일 인증
                    .loginId("user2")
                    .password(pw)
                    .nickName("심사임당")
                    .email("abc12@naver.com")
                    .birthDate(LocalDate.of(1995,1,1))
                    .gender(GenderType.F)
                    .joinMethod(JoinType.Normal)
                    .authentication(1)
                    .role(RoleType.User)
                    .city("서울특별시")
                    .district("서대문구")
                    .build();
            User user3 = User.builder() //소셜 회원가입자 + 추가정보 미입력
                    .loginId("user3")
                    .password(pw)
                    .name("이순신")
                    .nickName("이순신")
                    .email("abc13@naver.com")
                    .joinMethod(JoinType.Google)
                    .authentication(1)
                    .role(RoleType.Unauth)
                    .build();

            em.persist(user1);
            em.persist(user2);
            em.persist(user3);
        }
    }

}
