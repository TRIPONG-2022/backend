package tripong.backend.repository.admin.post;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import tripong.backend.dto.admin.post.GetPostPageANDSearchResponseDto;
import tripong.backend.dto.admin.post.GetPostReportedPageANDSearchResponseDto;
import tripong.backend.dto.admin.post.QGetPostPageANDSearchResponseDto;
import tripong.backend.dto.admin.post.QGetPostReportedPageANDSearchResponseDto;
import tripong.backend.dto.search.SearchAdminPostType;
import tripong.backend.entity.post.QPost;
import tripong.backend.entity.report.QPostReport;
import tripong.backend.entity.user.QUser;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdminPostSearchRepository {
    private final JPAQueryFactory queryFactory;
    QPost post = QPost.post;
    QUser user = QUser.user;
    QPostReport postReport = QPostReport.postReport;

    //게시글 전체 + 검색
    public Page<GetPostPageANDSearchResponseDto> getPostPageANDSearch(SearchAdminPostType searchType, String keyword, Pageable pageable) {
        List<GetPostPageANDSearchResponseDto> content = queryFactory
                .select(new QGetPostPageANDSearchResponseDto(
                        post.author.id, post.id, post.title, post.author.loginId, post.author.nickName, post.createdDate, post.category
                ))
                .from(post)
                .leftJoin(post.author, user)
                .where(isSearchable(searchType, keyword, post))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(isSearchable(searchType, keyword, post));
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    //신고 게시글 전체 + 검색
    public Page<GetPostReportedPageANDSearchResponseDto> getPostReportedPageANDSearch(SearchAdminPostType searchType, String keyword, Pageable pageable) {
        List<GetPostReportedPageANDSearchResponseDto> content = queryFactory
                .select(new QGetPostReportedPageANDSearchResponseDto(
                        postReport.reportedPostId.id, postReport.kind, postReport.reportedPostId.title
                        ,postReport.reportedPostId.createdDate, postReport.reportedPostId.author.id
                        ,postReport.reportedPostId.author.loginId, postReport.reportedPostId.author.nickName
                        ,postReport.reportUserId.loginId, postReport.createdDate, postReport.reportedPostId.category))
                .from(postReport)
                .leftJoin(postReport.reportedPostId, post)
                .leftJoin(postReport.reportUserId, user)
                .where(isSearchable(searchType, keyword, post))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(isSearchable(searchType, keyword, post));
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }


    private BooleanExpression isSearchable(SearchAdminPostType searchType, String keyword, QPost from){
        if (searchType == SearchAdminPostType.loginId){
            return loginIdEq(keyword, from);
        }
        else if (searchType == SearchAdminPostType.nickName){
            return nickNameEq(keyword, from);
        }
        else if (searchType == SearchAdminPostType.title){
            return titleCt(keyword, from);
        }
        else return null;
    }
    private BooleanExpression loginIdEq(String keyword, QPost from){
        return StringUtils.hasText(keyword) ? from.author.loginId.eq(keyword):null;
    }
    private BooleanExpression nickNameEq(String keyword, QPost from) {
        return StringUtils.hasText(keyword) ? from.author.nickName.eq(keyword):null;
    }
    private BooleanExpression titleCt(String keyword, QPost from){
        return StringUtils.hasText(keyword) ? post.title.contains(keyword):null;
    }

}
