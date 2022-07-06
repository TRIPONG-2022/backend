package tripong.backend.entity.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    review("CATEGORY_REVIEW", "후기, 리뷰"),
    board("CATEGORY_BOARD", "자유 게시판"),
    gathering("CATEGORY_GATHERING","모임 모집"),
    qna("CATEGORY_QNA","Q & A");

    private final String key;
    private final String title;
}
