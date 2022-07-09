package tripong.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.config.auth.authorization.CustomFilterInvocationSecurityMetadataSource;
import tripong.backend.entity.role.*;
import tripong.backend.entity.user.GenderType;
import tripong.backend.entity.user.JoinType;
import tripong.backend.entity.user.User;
import tripong.backend.repository.admin.resource.ResourceRepository;
import tripong.backend.repository.admin.role.RoleRepository;

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
    public void init() throws Exception {
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
        private final ResourceRepository resourceRepository;
        private final CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;


        public void init0(){ //초기 ROLE
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


            List<RoleResource> roleResources = new ArrayList<>(); //관리자 전용 리소스
            RoleResource roleResource1 = RoleResource.builder()
                    .role(admin)
                    .build();
            roleResources.add(roleResource1);

            Resource admin_url = Resource.builder()
                    .resourceName("/admin/**")
                    .resourceType(ResourceType.Url)
                    .roleResources(roleResources)
                    .priorityNum(1)
                    .build();
            resourceRepository.save(admin_url);

            List<RoleResource> roleResources1 = new ArrayList<>();
            RoleResource roleResource2 = RoleResource.builder()
                    .role(user)
                    .build();
            RoleResource roleResource3 = RoleResource.builder()
                            .role(unauth).build();
            roleResources1.add(roleResource2);
            roleResources1.add(roleResource3);

            Resource user_rul = Resource.builder()
                    .resourceName("/user&unauth/**")
                    .resourceType(ResourceType.Url)
                    .roleResources(roleResources1)
                    .priorityNum(1)
                    .build();
            resourceRepository.save(user_rul);

            List<RoleResource> roleResources3 = new ArrayList<>();
            RoleResource roleResource4 = RoleResource.builder()
                    .role(admin).build();
            roleResources3.add(roleResource4);

//            Resource user_rul1 = Resource.builder()
//                    .resourceName("tripong.backend.service.report.ReportService.userReport")
//                    .resourceType(ResourceType.Method)
//                    .roleResources(roleResources3)
//                    .priorityNum(1)
//                    .build();
//            resourceRepository.save(user_rul1);
        }

        public void init1() throws Exception {
            String pw = passwordEncoder.encode("1234");

            Role role_user = roleRepository.findByRoleName("ROLE_USER");
            Role role_unauth = roleRepository.findByRoleName("ROLE_UNAUTH");
            Role role_admin = roleRepository.findByRoleName("ROLE_ADMIN");

            UserRole userRole1 = UserRole.builder().role(role_unauth).build(); //user1 추가정보 미입력자 용
            UserRole userRole2 = UserRole.builder().role(role_user).build(); //user2 추가정보 입력자 용
            UserRole userRole3 = UserRole.builder().role(role_admin).build(); //admin 용

            List<UserRole> only_unauth_userRoles = new ArrayList<>();
            only_unauth_userRoles.add(userRole1);
            User user1 = User.builder() //일반 회원가입자 + 추가정보 미입력 + 이메일 미인증
                    .loginId("user1")
                    .password(pw)
                    .nickName("홍길동")
                    .email("abc11@naver.com")
                    .joinMethod(JoinType.Normal)
                    .authentication(0)
                    .userRoles(only_unauth_userRoles)
                    .build();

            List<UserRole> only_user_userRoles = new ArrayList<>();
            only_user_userRoles.add(userRole2);
            User user2 = User.builder() //일반 회원가입 + 추가정보 입력 + 이메일 인증
                    .loginId("user2")
                    .password(pw)
                    .nickName("심사임당")
                    .email("abc12@naver.com")
                    .birthDate(LocalDate.of(1995,1,1))
                    .gender(GenderType.F)
                    .joinMethod(JoinType.Normal)
                    .authentication(1)
                    .userRoles(only_user_userRoles)
                    .city("서울특별시")
                    .district("서대문구")
                    .build();

            UserRole userRole4 = UserRole.builder().role(role_unauth).build();
            List<UserRole> only_unauth_userRoles2 = new ArrayList<>();
            only_unauth_userRoles2.add(userRole4);
            User user3 = User.builder() //소셜 회원가입자 + 추가정보 미입력
                    .loginId("user3")
                    .password(pw)
                    .name("이순신")
                    .nickName("이순신")
                    .email("abc13@naver.com")
                    .joinMethod(JoinType.Google)
                    .authentication(1)
                    .userRoles(only_unauth_userRoles2)
                    .build();


            List<UserRole> admin_userRoles = new ArrayList<>();
            admin_userRoles.add(userRole3);
            UserRole userRole5 = UserRole.builder().role(role_user).build();
            admin_userRoles.add(userRole5);
            User admin = User.builder() //관리자용
                    .loginId("admin")
                    .password(pw)
                    .name("관리자")
                    .nickName("관리자")
                    .email("admin@naver.com")
                    .birthDate(LocalDate.of(1995,1,1))
                    .gender(GenderType.M)
                    .joinMethod(JoinType.Normal)
                    .authentication(1)
                    .userRoles(admin_userRoles)
                    .city("서울특별시")
                    .district("서대문구")
                    .build();


            em.persist(user1);
            em.persist(user2);
            em.persist(user3);
            em.persist(admin);
            customFilterInvocationSecurityMetadataSource.reload_url();
        }
    }

}
