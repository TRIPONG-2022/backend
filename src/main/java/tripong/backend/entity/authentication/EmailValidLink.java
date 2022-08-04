package tripong.backend.entity.authentication;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailValidLink {

    // 이메일 토큰 만료 시간 상수 설정
    private static final long EMAIL_VALID_LINK_EXPRIRATION_TIME_VALUE = 3L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    @Column(nullable = false)
    private String userId;

    private LocalDateTime createdTime;

    private LocalDateTime expirationDate;

    private boolean expired;

    // 이메일 유효링크 생성
    public static EmailValidLink createEmailValidLink(String userId) {
        EmailValidLink validLink = new EmailValidLink();
        validLink.createdTime = LocalDateTime.now();
        validLink.expirationDate = LocalDateTime.now().plusMinutes(EMAIL_VALID_LINK_EXPRIRATION_TIME_VALUE);
        validLink.expired = false;
        validLink.userId = userId;
        return validLink;
    }

    // 이메일 유료링크 만료 설정
    public void makeInvalidLink(){
        this.expired = true;
    }

}
