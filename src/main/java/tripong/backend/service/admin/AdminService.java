package tripong.backend.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.dto.admin.post.GetPostReportedListResponseDto;
import tripong.backend.dto.admin.user.GetUserAllListDto;
import tripong.backend.dto.admin.user.GetUserReportedListResponseDto;
import tripong.backend.dto.admin.user.UpdateRolesRequestDto;
import tripong.backend.entity.report.PostReport;
import tripong.backend.entity.report.UserReport;
import tripong.backend.entity.role.Role;
import tripong.backend.entity.role.UserRole;
import tripong.backend.entity.user.User;
import tripong.backend.repository.admin.role.RoleRepository;
import tripong.backend.repository.admin.role.UserRoleRepository;
import tripong.backend.repository.report.PostReportRepository;
import tripong.backend.repository.report.UserReportRepository;
import tripong.backend.repository.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final UserReportRepository userReportRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PostReportRepository postReportRepository;


    /**
     * 사용자 전체 목록
     */
    public Page<GetUserAllListDto> getUserList(Pageable pageable) {
        log.info("시작: AdminService 전체사용자리스트");

        Page<User> page = userRepository.findPagingAll(pageable);

        log.info("종료: AdminService 전체사용자리스트");
        return page.map(p -> new GetUserAllListDto(p));
    }


    /**
     * 신고 받은 사용자 전체 목록
     * 반환: 신고받은 유저 pk, 신고 이유, 신고받은 유저이름, 신고받은 유저닉네임, 신고받은 유저아이디, 신고받은 유저권한들, + 신고자(아이디)
     *  -유저 pk: 추후 권한 수정을 pk로 처리하기 위해 반환
     */
    public Page<GetUserReportedListResponseDto> getUserReportedList(Pageable pageable) {
        Page<UserReport> page = userReportRepository.findReportUserANDReportedUserPagingAll(pageable);
        return page.map(ur -> new GetUserReportedListResponseDto(ur));
    }

    /**
     * 신고받은 유저 BLACK 처리
     * - 가지고 있던 모든 권한 삭제 후, BLACK 부여
     * - 존재하지 않는 유저 에러 처리
     */
    @Transactional
    public void changedBlack(Long userId) {
        log.info("시작: AdminService 신고유저블랙");
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            deleteRoles(user);
            List<UserRole> newUserRoles = new ArrayList<>();
            
            Role role_black = roleRepository.findByRoleName("ROLE_BLACK");
            UserRole userRole_black = UserRole.builder().role(role_black).build();
            
            newUserRoles.add(userRole_black);
            user.get().addUserRole(newUserRoles);
        }
        else{
            throw new IllegalStateException("존재하지 않는 유저입니다.");
        }
        log.info("종료: AdminService 신고유저블랙");
    }

    private void deleteRoles(Optional<User> user) {
        List<UserRole> userRoles = user.get().getUserRoles();
        userRoleRepository.deleteAllInBatch(userRoles);
    }



    /**
     * 사용자 권한 변경
     */
    @Transactional
    public void changedRoles(Long userId, UpdateRolesRequestDto dto) {
        log.info("시작: AdminService 사용자권한변경");

        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            deleteRoles(user);
            List<UserRole> newUserRoles = new ArrayList<>();
            dto.getRoleNames().stream().forEach( r->
                    {
                        Role roleName = roleRepository.findByRoleName(r);
                        UserRole userRole = UserRole.builder().role(roleName).build();
                        newUserRoles.add(userRole);
                    }
            );
            user.get().addUserRole(newUserRoles);
        }
        else{
            throw new IllegalStateException("존재하지 않는 유저입니다.");
        }
        log.info("종료: AdminService 사용자권한변경");
    }


    /**
     * 신고 받은 게시글 전체 목록
     * 반환: 게시글 pk, 신고 이유, 게시글 제목, 게시글 작성 시간, 작성자 pk, 작성자 아이디, 작성자 닉네임, 신고자 아이디, 신고 시간
     *  -게시글 pk: 삭제 위해 반환
     *  -작성자 pk: 추후 권한 수정을 pk로 처리하기 위해 반환
     */
    public Page<GetPostReportedListResponseDto> getPostReportedList(Pageable pageable) {
        Page<PostReport> page = postReportRepository.findReportPostANDReportedPostANDReportUserPagingAll(pageable);
        return page.map(pr -> new GetPostReportedListResponseDto(pr));
    }

    /**
     * 신고 받은 게시글 삭제
     */
    @Transactional
    public void deletePost(Long postId) {
        //포스트 리파지토리  머지 후 삭제.
    }
}

