package tripong.backend.exception.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostErrorMessage {
    USER_ID_NOT_MATCH("존재하지 않는 사용자입니다.", 201),
    POST_ID_NOT_MATCH("존재하지 않는 게시물입니다.", 202);

    private final String message;
    private final int code;
}
