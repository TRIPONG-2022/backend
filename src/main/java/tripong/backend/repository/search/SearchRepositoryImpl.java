package tripong.backend.repository.search;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import tripong.backend.dto.search.SearchType;
import tripong.backend.entity.post.Post;
import tripong.backend.entity.post.QPost;

import java.util.List;

@RequiredArgsConstructor
public class SearchRepositoryImpl implements SearchRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QPost post = new QPost("d");

    @Override
    public Page<Post> search(SearchType searchType, String keyword, Pageable pageable) {
                QueryResults<Post> result = queryFactory
                .selectFrom(post)
                .where(isSearchable(searchType, keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

                List<Post> content = result.getResults();
                Long total = result.getTotal();

                return new PageImpl<>(content, pageable, total);
    }

    BooleanExpression authorEq(String keyword){
        return post.author.name.eq(keyword);
    }

    BooleanExpression titleCt(String keyword){
        return post.title.contains(keyword);
    }

    BooleanExpression contentCt(String keyword){
        return post.content.contains(keyword);
    }

    BooleanExpression isSearchable(SearchType searchType, String keyword){
        if (searchType == SearchType.USER){
            return authorEq(keyword);
        }
        else if (searchType == SearchType.TITLE){
            return titleCt(keyword);
        }
        else {
            return contentCt(keyword);
        }
    }


}
