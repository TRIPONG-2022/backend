package tripong.backend.repository.admin.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import tripong.backend.dto.admin.user.GetUserPageANDSearchResponseDto;
import tripong.backend.dto.admin.user.GetUserReportedPageANDSearchResponseDto;
import tripong.backend.dto.admin.user.QGetUserPageANDSearchResponseDto;
import tripong.backend.dto.admin.user.QGetUserReportedPageANDSearchResponseDto;
import tripong.backend.dto.search.SearchAdminUserType;
import tripong.backend.entity.report.QUserReport;
import tripong.backend.entity.user.QUser;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdminUserSearchRepository {
    private final JPAQueryFactory queryFactory;
    QUser user = QUser.user;
    QUserReport userReport = QUserReport.userReport;

    //유저 전체 + 검색
    public Page<GetUserPageANDSearchResponseDto> getUserPageANDSearch(SearchAdminUserType searchType, String keyword, Pageable pageable){
        List<GetUserPageANDSearchResponseDto> content = queryFactory
                .select(new QGetUserPageANDSearchResponseDto(user))
                .from(user)
                .where(isSearchable(searchType, keyword, user))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> countQuery = queryFactory
                .select(user.count())
                .from(user)
                .where(isSearchable(searchType, keyword, user));
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }


    //신고 전체 + 검색
    public Page<GetUserReportedPageANDSearchResponseDto> getUserReportedPageANDSearch(SearchAdminUserType searchType, String keyword, Pageable pageable) {
        List<GetUserReportedPageANDSearchResponseDto> content = queryFactory
                .select(new QGetUserReportedPageANDSearchResponseDto(userReport))
                .from(userReport)
                .where(isSearchable(searchType, keyword, userReport.reportedUserId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> countQuery = queryFactory
                .select(userReport.count())
                .from(userReport)
                .where(isSearchable(searchType, keyword, userReport.reportedUserId));
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }


    private BooleanExpression isSearchable(SearchAdminUserType searchType, String keyword, QUser from){
        if (searchType == SearchAdminUserType.loginId){
            return loginIdEq(keyword, from);
        }
        else if (searchType == SearchAdminUserType.nickName){
            return nickNameEq(keyword, from);
        }
        else return null;
    }
    private BooleanExpression loginIdEq(String keyword, QUser from){
        return StringUtils.hasText(keyword) ? from.loginId.eq(keyword):null;
    }
    private BooleanExpression nickNameEq(String keyword, QUser from) {
        return StringUtils.hasText(keyword) ? from.nickName.eq(keyword):null;
    }


}
