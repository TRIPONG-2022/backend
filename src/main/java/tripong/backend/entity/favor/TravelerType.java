package tripong.backend.entity.favor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TravelerType {

    extravertedJudger("EXTRAVERTED_JUDGER", "계획형 인싸 여행가"),
    introvertedJudger("INTROVERTED_JUDGER", "계획형 앗싸 여행가"),
    extravertedPerceiver("EXTRAVERTED_PERCEIVER", "자유로운 인싸 여행가"),
    introvertedPerceiver("INTROVERTED_PERCEIVER", "자유로운 앗싸 여행가");

    private final String key;
    private final String type;
}
