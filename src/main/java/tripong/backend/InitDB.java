package tripong.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.entity.role.Resource;
import tripong.backend.entity.role.ResourceType;
import tripong.backend.entity.role.Role;
import tripong.backend.entity.role.RoleResource;
import tripong.backend.entity.user.User;
import tripong.backend.entity.user.GenderType;
import tripong.backend.entity.user.JoinType;
import tripong.backend.entity.user.RoleType;
import tripong.backend.repository.role.ResourceRepository;
import tripong.backend.repository.role.RoleRepository;
import tripong.backend.repository.user.UserRepository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;


    @PostConstruct
    public void init(){
        initService.init0();
        initService.init1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;
        private final BCryptPasswordEncoder passwordEncoder;
        private final RoleRepository roleRepository;
        private final UserRepository userRepository;
        private final ResourceRepository resourceRepository;


        public void init0(){ //초기 세팅: 관리자만
            Role admin = Role.builder()
                    .roleName("ROLE_ADMIN")
                    .description("관리자")
                    .build();
            roleRepository.save(admin);
            Role unauth = Role.builder()
                    .roleName("ROLE_UNAUTH")
                    .description("이메일_미인증_유저")
                    .build();
            roleRepository.save(unauth);
            Role user = Role.builder()
                    .roleName("ROLE_USER")
                    .description("이메일_인증_유저")
                    .build();
            roleRepository.save(user);
            Role black = Role.builder()
                    .roleName("ROLE_BlACK")
                    .description("블랙리스트_유저")
                    .build();
            roleRepository.save(black);


            List<RoleResource> roleResources = new ArrayList<>();
            RoleResource roleResource1 = RoleResource.builder()
                    .role(admin)
                    .build();
            roleResources.add(roleResource1);

            Resource admin_url = Resource.builder()
                    .resourceName("/admin/**")
                    .methodName("")
                    .resourceType(ResourceType.Url)
                    .roleResources(roleResources)
                    .priorityNum(1)
                    .build();
            resourceRepository.save(admin_url);

        }

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
