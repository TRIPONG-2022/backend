package tripong.backend.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.dto.admin.user.GetUserReportedListResponseDto;
import tripong.backend.entity.report.UserReport;
import tripong.backend.repository.report.UserReportRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final UserReportRepository userReportRepository;

    /**
     * 신고 받은 사용자 전체 목록
     * 반환: 신고받은 유저 pk, 신고 이유, 신고받은 유저이름, 신고받은 유저닉네임, 신고받은 유저아이디, 신고받은 유저권한들, + 신고자(아이디)
     *  -유저 pk: 추후 권한 수정을 pk로 처리하기 위해 반환
     */
    public Page<GetUserReportedListResponseDto> getUserReportedList(Pageable pageable) {
        Page<UserReport> page = userReportRepository.findAll(pageable);
        List<GetUserReportedListResponseDto> result = page.stream().map(ur -> new GetUserReportedListResponseDto(ur))
                .collect(Collectors.toList());


        return new PageImpl<>(result, pageable, page.getTotalElements());
    }
}
