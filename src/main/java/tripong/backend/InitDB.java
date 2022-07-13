package tripong.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.config.security.authorization.CustomFilterInvocationSecurityMetadataSource;
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
            Role admin = new Role("ROLE_ADMIN", "관리자");
            Role unauth = new Role("ROLE_UNAUTH", "이메일 미인증자");
            Role user = new Role("ROLE_USER", "이메일 인증자");
            Role black = new Role("ROLE_BLACK", "블랙리스트");
            roleRepository.save(admin);
            roleRepository.save(unauth);
            roleRepository.save(user);
            roleRepository.save(black);


            List<RoleResource> roleResources1 = new ArrayList<>(); //관리자 전용 페이지 리소스
            roleResources1.add(new RoleResource(admin));
            resourceRepository.save(new Resource("/admin/**", ResourceType.Url, "관리자페이지", 1, roleResources1));


//            List<RoleResource> roleResources2 = new ArrayList<>(); //유저 신고 기능 리소스
//            roleResources2.add(new RoleResource(user));
//            roleResources2.add(new RoleResource(admin));
//            resourceRepository.save(new Resource("tripong.backend.service.report.ReportService.userReport", ResourceType.Method, "유저신고기능", 1, roleResources2));
        }

        public void init1() throws Exception {
            String pw = passwordEncoder.encode("1234");

            Role role_user = roleRepository.findByRoleName("ROLE_USER");
            Role role_unauth = roleRepository.findByRoleName("ROLE_UNAUTH");
            Role role_admin = roleRepository.findByRoleName("ROLE_ADMIN");

            UserRole user1_userRole = new UserRole(role_unauth); //user1 추가정보 미입력자 용
            UserRole user2_userRole = new UserRole(role_user); //user2 추가정보 입력자 용
            UserRole user3_userRole = new UserRole(role_unauth); //user3 소셜 회원가입자 + 추가정보 미입력
            UserRole user4_userRole = new UserRole(role_user); //탈퇴회원 용
            UserRole admin1_userRole = new UserRole(role_admin); //admin 용


            List<UserRole> user1_userRoleList = new ArrayList<>();
            user1_userRoleList.add(user1_userRole);
            User user1 = User.builder() //일반 회원가입자 + 추가정보 미입력 + 이메일 미인증
                    .loginId("user1")
                    .password(pw)
                    .nickName("홍길동")
                    .email("alicesykim95@gmail.com")
                    .joinMethod(JoinType.Normal)
                    .authentication(0)
                    .userRoles(user1_userRoleList)
                    .build();

            List<UserRole> user2_userRoleList = new ArrayList<>();
            user2_userRoleList.add(user2_userRole);
            User user2 = User.builder() //일반 회원가입 + 추가정보 입력 + 이메일 인증
                    .loginId("user2")
                    .password(pw)
                    .nickName("심사임당")
                    .email("abc12@naver.com")
                    .birthDate(LocalDate.of(1995,1,1))
                    .gender(GenderType.F)
                    .joinMethod(JoinType.Normal)
                    .authentication(1)
                    .userRoles(user2_userRoleList)
                    .city("서울특별시")
                    .district("서대문구")
                    .build();

            List<UserRole> user3_userRoleList = new ArrayList<>();
            user3_userRoleList.add(user3_userRole);
            User user3 = User.builder() //소셜 회원가입자 + 추가정보 미입력
                    .loginId("user3")
                    .password(pw)
                    .name("이순신")
                    .nickName("이순신")
                    .email("abc13@naver.com")
                    .joinMethod(JoinType.Google)
                    .authentication(1)
                    .userRoles(user3_userRoleList)
                    .build();


            List<UserRole> admin1_userRoleList = new ArrayList<>();
            UserRole add_userRole = new UserRole((role_user));
            admin1_userRoleList.add(admin1_userRole);
            admin1_userRoleList.add(add_userRole);
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
                    .userRoles(admin1_userRoleList)
                    .city("서울특별시")
                    .district("서대문구")
                    .build();

            List<UserRole> withdrawal_userRoleList = new ArrayList<>();
            withdrawal_userRoleList.add(user4_userRole);
            User user4 = User.builder()
                    .loginId("탈퇴 회원")
                    .nickName("탈퇴 회원")
                    .password(pw)
                    .email("탈퇴 회원")
                    .userRoles(withdrawal_userRoleList)
                    .joinMethod(JoinType.Normal).build();


            em.persist(user1);
            em.persist(user2);
            em.persist(user3);
            em.persist(admin);
            em.persist(user4);
            customFilterInvocationSecurityMetadataSource.reload_url();
        }
    }

}
