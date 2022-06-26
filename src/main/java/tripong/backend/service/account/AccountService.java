package tripong.backend.service.account;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.config.auth.jwt.JwtProperties;
import tripong.backend.config.auth.oauth.GoogleUser;
import tripong.backend.config.auth.oauth.OAuthInfo;
import tripong.backend.dto.account.OauthJoinRequestDto;
import tripong.backend.entity.user.JoinType;
import tripong.backend.entity.user.User;
import tripong.backend.dto.account.NormalJoinRequestDto;
import tripong.backend.repository.user.UserRepository;

import java.util.Date;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

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

        boolean loginId_dup = userRepository.existsByLoginId(dto.getLoginId());
        boolean nickName_dub = userRepository.existsByNickName(dto.getNickName());
        boolean email_dub = userRepository.existsByEmail(dto.getEmail());

        if(email_dub){
            throw new IllegalStateException("이미 해당 이메일로 계정이 존재");
        }
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

//
//    @Transactional
//    public String googleJoin(GoogleUser googleInfo) {
//        String LoginId = googleInfo.getProviderName() +"_"+googleInfo.getNickName() + googleInfo.getProviderId();
//
//        User user =userRepository.findByLoginId(LoginId).orElseGet(()-> {
//            OauthJoinRequestDto dto = new OauthJoinRequestDto();
//            dto.setLoginId(LoginId);
//            dto.setPassword(encoder.encode(sKey + googleInfo.getProviderId()));
//            dto.setEmail(googleInfo.getEmail());
//            dto.setNickName(LoginId);
//            dto.setJoinMethod(JoinType.Google);
//            User yet = dto.toEntity();
//            userRepository.save(yet);
//
//            return yet;
//        });
//
//
//        return JwtCreate(user);
//    }
//    public String JwtCreate(User user){
//        String jwtToken = JWT.create()
//                .withSubject(user.getLoginId()) //토큰명
//                .withExpiresAt(new Date(System.currentTimeMillis()+ (JwtProperties.EXPIRATION_TIME))) //만료시간 10분
//                .withClaim("loginId", user.getLoginId())
//                .sign(Algorithm.HMAC512(JwtProperties.SECRET)); //HMAC HS256에 쓰일 개인키
//        return jwtToken;
//    }
}
