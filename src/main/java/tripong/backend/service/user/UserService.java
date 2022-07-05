package tripong.backend.service.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.entity.post.Post;
import tripong.backend.repository.post.PostRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PostRepository postRepository;
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void test1() {
        Post post1 = new Post();
        Post post2 = new Post();
        postRepository.save(post1);
        postRepository.save(post2);
        
        em.flush();em.clear();
        List<Post> all = postRepository.findAll();
        all.stream().forEach(r -> System.out.println("r.getUser() + r.getTitle() = " + r.getUser() + r.getTitle()));
    }

}
