package tripong.backend.service.redis;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RedisService {

    @Resource(name = "redisTemplate")
    ValueOperations<String, String> operations;

    public Long incVisitCount(Long postId) {
        Long count = 0L;
        try {
            operations.increment("viewCount::"+postId, 1);
            count = Long.valueOf(operations.get("viewCount::"+postId));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return count;
    }

    public Long getVisitCount(String key) {
        try {
            return Long.valueOf(operations.get(key));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return 0L;
    }
}
