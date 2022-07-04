package tripong.backend.scheduler.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tripong.backend.service.post.PostService;
import tripong.backend.service.redis.RedisService;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class RedisScheduler {

    private final RedisTemplate<String, String> redisTemplate;

    private final PostService postService;

    private final RedisService redisService;

    /* 5초마다 실행 */
    @Scheduled(fixedDelay = 5000)
    public void PostExpire() {
        Set<String> redisKeys = redisTemplate.keys("viewCount*");
        for (String key : redisKeys) {
            Long newViewCount = redisService.getVisitCount(key);
            postService.updateViewCount(Long.valueOf(key.split("::")[1]), newViewCount);
            redisTemplate.delete(key);
        }
    }
}
