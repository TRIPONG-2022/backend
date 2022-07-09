package tripong.backend.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tripong.backend.dto.search.SearchType;
import tripong.backend.entity.post.Post;

public interface SearchRepositoryCustom {
    Page<Post> search(SearchType searchType, String keyword, Pageable pageable);
}
