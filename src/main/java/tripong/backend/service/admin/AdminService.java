package tripong.backend.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.config.security.authentication.token.RefreshTokenProperties;
import tripong.backend.dto.admin.post.GetPostPageANDSearchResponseDto;
import tripong.backend.dto.admin.post.GetPostReportedPageANDSearchResponseDto;
import tripong.backend.dto.admin.user.GetUserPageANDSearchResponseDto;
import tripong.backend.dto.admin.user.GetUserReportedPageANDSearchResponseDto;
import tripong.backend.dto.admin.user.UpdateRolesRequestDto;
import tripong.backend.dto.search.SearchAdminPostType;
import tripong.backend.dto.search.SearchAdminUserType;
import tripong.backend.entity.role.UserRole;
import tripong.backend.entity.user.User;
import tripong.backend.repository.admin.post.AdminPostSearchRepository;
import tripong.backend.repository.admin.role.RoleRepository;
import tripong.backend.repository.admin.role.UserRoleRepository;
import tripong.backend.repository.admin.user.AdminUserSearchRepository;
import tripong.backend.repository.post.PostRepository;
import tripong.backend.repository.report.PostReportRepository;
import tripong.backend.repository.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PostReportRepository postReportRepository;
    private final PostRepository postRepository;
    private final RedisTemplate redisTemplate;
    private final AdminUserSearchRepository adminUserSearchRepository;
    private final AdminPostSearchRepository adminPostSearchRepository;


    /**
     * 유저 전체 목록 + 검색(loginId, nickName)
     */
    public Page<GetUserPageANDSearchResponseDto> getUserPageANDSearch(SearchAdminUserType searchType, String keyword, Pageable pageable) {
        log.info("시작: AdminService 사용자 리스트 + 검색 (종료 log 없음)");
        return adminUserSearchRepository.getUserPageANDSearch(searchType, keyword, pageable);
    }


    /**
     * 신고 접수(유저) 전체 목록 + 검색(loginId, nickName)
     * 반환: 신고받은 유저 pk, 신고 이유, 신고받은 유저이름, 신고받은 유저닉네임, 신고받은 유저아이디, 신고받은 유저권한들, + 신고자(아이디)
     *  -유저 pk: 추후 권한 수정을 pk로 처리하기 위해 반환
     */
    public Page<GetUserReportedPageANDSearchResponseDto> getUserReportedPageANDSearch(SearchAdminUserType searchType, String keyword, Pageable pageable) {
        log.info("시작: AdminService 신고받은 사용자 리스트 + 검색 (종료 log 없음)");
        return adminUserSearchRepository.getUserReportedPageANDSearch(searchType, keyword, pageable);
    }

    /**
     * 유저 BLACK 처리
     * - 가지고 있던 모든 권한 삭제 후, BLACK 부여
     * - 존재하지 않는 유저 에러 처리
     */
    @Transactional
    public void changedBlack(Long userId) {
        log.info("시작: AdminService 신고유저블랙");
        User user = userRepository.findRolesUpdateById(userId).orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다. userId=" + userId));
        deleteRoles(user);
        List<UserRole> newUserRoles = new ArrayList<>();
        newUserRoles.add(new UserRole(roleRepository.findByRoleName("ROLE_BLACK").get()));
        user.addUserRole(newUserRoles);
        String roles = user.getUserRoles().stream().map(r-> r.getRole().getRoleName()).collect(Collectors.joining(","));
        redisTemplate.opsForValue().set("RoleUpdate:"+ user.getLoginId(), roles, RefreshTokenProperties.EXPIRATION_TIME, TimeUnit.MILLISECONDS);
        log.info("종료: AdminService 신고유저블랙");
    }

    private void deleteRoles(User user) {
        List<UserRole> userRoles = user.getUserRoles();
        userRoleRepository.deleteAllInBatch(userRoles);
    }



    /**
     * 유저 권한 변경
     */
    @Transactional
    public void changedRoles(Long userId, UpdateRolesRequestDto dto) {
        log.info("시작: AdminService 사용자권한변경");
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다. userId=" + userId));
        deleteRoles(user);
        List<UserRole> newUserRoles = new ArrayList<>();
        dto.getRoleNames().stream().forEach( r-> newUserRoles.add(new UserRole(roleRepository.findByRoleName(r).get())));
        user.addUserRole(newUserRoles);
        String roles = user.getUserRoles().stream().map(r-> r.getRole().getRoleName()).collect(Collectors.joining(","));
        redisTemplate.opsForValue().set("RoleUpdate:"+ user.getLoginId(), roles, RefreshTokenProperties.EXPIRATION_TIME, TimeUnit.MILLISECONDS);
        log.info("종료: AdminService 사용자권한변경");
    }


    /**
     * 게시글 전체 목록 + 검색
     */
    public Page<GetPostPageANDSearchResponseDto> getPostPageANDSearch(SearchAdminPostType searchType, String keyword, Pageable pageable){
        log.info("시작: AdminService 게시글 전체 목록 + 검색 (종료 log 없음)");
        return adminPostSearchRepository.getPostPageANDSearch(searchType, keyword, pageable);
    }

    /**
     * 신고 접수 게시글 전체 목록 + 검색
     * 반환: 게시글 pk, 신고 이유, 게시글 제목, 게시글 작성 시간, 작성자 pk, 작성자 아이디, 작성자 닉네임, 신고자 아이디, 신고 시간
     *  -게시글 pk: 삭제 위해 반환
     *  -작성자 pk: 추후 권한 수정을 pk로 처리하기 위해 반환
     */
    public Page<GetPostReportedPageANDSearchResponseDto> getPostReportedPageANDSearch(SearchAdminPostType searchType, String keyword, Pageable pageable){
        log.info("시작: AdminService 게시글 전체 목록 + 검색 (종료 log 없음)");
        return adminPostSearchRepository.getPostReportedPageANDSearch(searchType, keyword, pageable);
    }
}

