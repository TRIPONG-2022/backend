package tripong.backend.entity.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    REVIEW("CATEGORY_REVIEW", "후기, 리뷰"),
    BOARD("CATEGORY_BOARD", "자유 게시판"),
    GATHERING("CATEGORY_GATHERING","모임 모집"),
    QNA("CATEGORY_QNA","Q & A");

    private final String key;
    private final String title;
}
