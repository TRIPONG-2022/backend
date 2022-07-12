package tripong.backend.service.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.config.security.principal.PrincipalDetail;
import tripong.backend.config.security.oauth.oauthDetail.OAuthInfo;
import tripong.backend.dto.account.FirstExtraInfoPutRequestDto;
import tripong.backend.dto.account.NormalJoinRequestDto;
import tripong.backend.dto.account.OauthJoinRequestDto;
import tripong.backend.entity.role.Role;
import tripong.backend.entity.role.UserRole;
import tripong.backend.entity.user.JoinType;
import tripong.backend.entity.user.User;
import tripong.backend.exception.account.AccountErrorName;
import tripong.backend.repository.admin.role.RoleRepository;
import tripong.backend.repository.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final RoleRepository roleRepository;

    @Value("${tripong.skey}")
    private String sKey;

    /**
     * 일반 회원가입
     * -이메일 인증기반이므로, 이메일 중복 먼저 체크
     * -아이디와 닉네임 중복 체크
     */
    @Transactional
    public void normalJoin(NormalJoinRequestDto dto){
        log.info("시작: AccountService 일반회원가입");

        boolean email_dub = userRepository.existsByEmail(dto.getEmail());
        if(email_dub){
            throw new IllegalStateException(AccountErrorName.Email_DUP);
        }

        boolean loginId_dup = userRepository.existsByLoginId(dto.getLoginId());
        boolean nickName_dub = userRepository.existsByNickName(dto.getNickName());
        if(loginId_dup && nickName_dub){
            throw new IllegalStateException(AccountErrorName.LoginId_NickName_DUP);
        }
        if(loginId_dup){
            throw new IllegalStateException(AccountErrorName.LoginId_DUP);
        }
        if(nickName_dub){
            throw new IllegalStateException(AccountErrorName.NickName_DUP);
        }

        dto.setPassword(encoder.encode(dto.getPassword()));
        User user = new User(dto.getLoginId(), dto.getPassword(), dto.getNickName(), dto.getEmail(), JoinType.Normal);
        authorize_UNAUTH(user);
        userRepository.save(user);
        log.info("종료: AccountService 일반회원가입");
    }

    /**
     * 소셜 회원가입
     * -일반 회원가입시, 아이디와 닉네임을 소셜명으로 시작을 금지시키고
     *  소셜 회원가입시에 아이디와 닉네임을 소셜 이름+ 소셜에서의 닉네임 +소셜 id 로 설정해 중복방지
     * -소셜 회원가입자는 이메일 인증 처리
     * */
    @Transactional
    public User oauthJoin(OAuthInfo oAuthInfo){
        log.info("시작: AccountService 소셜회원가입");

        OauthJoinRequestDto dto = new OauthJoinRequestDto();
        dto.setLoginId(oAuthInfo.getProviderName() + "_" + oAuthInfo.getNickName() + oAuthInfo.getProviderId());
        dto.setPassword(encoder.encode(sKey + oAuthInfo.getProviderId()));
        dto.setEmail(oAuthInfo.getEmail());
        dto.setNickName(oAuthInfo.getProviderName() + "_" + oAuthInfo.getNickName() + oAuthInfo.getProviderId());
        dto.setJoinMethod(getJoin(oAuthInfo.getProviderName()));
        dto.setUserRoles(authorize_oauthJoin_userRoles());
        User yet = dto.toEntity();

        log.info("종료: AccountService 소셜회원가입");
        return userRepository.save(yet);
    }

    private JoinType getJoin(String providerName) {
        JoinType joinType = JoinType.Normal;
        if(providerName == "google"){
            joinType = JoinType.Google;
        }
        else if(providerName == "facebook"){
            joinType = JoinType.FaceBook;
        }
        else if(providerName == "naver"){
            joinType = JoinType.Naver;
        }
        else if(providerName == "kakao"){
            joinType = JoinType.KaKao;
        }
        return joinType;
    }

    /**
     * 추가정보 입력
     * -필수: 이름, 성별, 생년월일, 도시(시), 도시(구)
     * -권한: Unauth -> User 변경
     */
    @Transactional
    public void firstExtraInfoPatch(FirstExtraInfoPutRequestDto dto, PrincipalDetail principal) {
        log.info("시작: AccountService 추가정보입력");
        User user = userRepository.findById(principal.getUser().getId()).orElseThrow(()->{
            return new IllegalStateException(AccountErrorName.PK_NOT_USER);
        });
        user.putExtraInfo(dto);
        authorize_USER(user);

        log.info("종료: AccountService 추가정보입력");
    }


    private void authorize_UNAUTH(User user){
        Role role_unauth = roleRepository.findByRoleName("ROLE_UNAUTH");
        List<UserRole> only_unauth_userRoles = new ArrayList<>();
        only_unauth_userRoles.add(new UserRole(role_unauth));
        user.addUserRole(only_unauth_userRoles);
    }
    private void authorize_USER(User user){
        Role role_user = roleRepository.findByRoleName("ROLE_USER");
        List<UserRole> only_user_userRoles = new ArrayList<>();
        only_user_userRoles.add(new UserRole(role_user));
        user.addUserRole(only_user_userRoles);
    }
    private List<UserRole> authorize_oauthJoin_userRoles() {
        Role role_user = roleRepository.findByRoleName("ROLE_USER");
        List<UserRole> only_user_userRoles = new ArrayList<>();
        only_user_userRoles.add(new UserRole(role_user));
        return only_user_userRoles;
    }


    /**
     * 회원 탈퇴
     * -이름, 아이디, 닉네임  = "탈퇴 회원"으로 변경
     * -이메일: 기존이메일 + 비밀키 (고유 이메일을 남겨 관리 위함)
     */
    @Transactional
    public void withdrawal(PrincipalDetail principal) {
        log.info("시작: AccountService 회원탈퇴");
        User user = userRepository.findById(principal.getUser().getId()).orElseThrow(()->{
            return new IllegalStateException("해당 유저 정보 없음");
        });
        user.account_withdrawal(sKey);
        log.info("종료: AccountService 회원탈퇴");
    }
}
