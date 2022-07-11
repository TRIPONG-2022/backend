package tripong.backend.service.reply;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.dto.reply.ReplyRequestDto;
import tripong.backend.dto.reply.ReplyResponseDto;
import tripong.backend.entity.reply.Reply;
import tripong.backend.entity.user.User;
import tripong.backend.repository.reply.ReplyRepository;
import tripong.backend.repository.user.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;

    // 내가쓴 댓글 및 대댓글 조회
    public List<ReplyResponseDto> getReplyListByUserId(String userId, LocalDateTime fromDate, LocalDateTime endDate, Pageable pageable){

        List<ReplyResponseDto> ReplyList = replyRepository.findReplyByUserId(userId, fromDate, endDate, pageable).stream()
                // .map(ReplyResponseDto::new)
                .collect(Collectors.toList());

        return ReplyList;

    }

    // 댓글 리스트
    public List<ReplyResponseDto> getListParentReply(Long postId, Pageable pageable){

        List<ReplyResponseDto> ReplyList = replyRepository.findParentReplyByPostId(postId, pageable).stream()
                .collect(Collectors.toList());

        return ReplyList;

    }

    // 대댓글 리스트
    public List<ReplyResponseDto> getListChildrenReply(Long postId, Long parentReply, Pageable pageable){

        List<ReplyResponseDto> ReplyList = replyRepository.findChildrenReplyByPostId(postId, parentReply, pageable).stream()
                .collect(Collectors.toList());

        return ReplyList;

    }

    // 댓글 및 대댓글 작성
    @Transactional
    public void saveReply(ReplyRequestDto dto){

        Reply reply = dto.toEntity();

        Optional<User> user = userRepository.findByLoginId(dto.getUserId());
        reply.setUserId(user.orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. userId=" + dto.getUserId())));

        if (dto.getParentReply() != null){
            reply.setParentReply(replyRepository.findById(dto.getParentReply()).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id =" + dto.getParentReply())));
        }

        replyRepository.save(reply);

    }

    // 댓글 및 대댓글 수정
    @Transactional
    public void updateReply(ReplyRequestDto dto){

        Reply reply = replyRepository.findById(dto.getId()).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + dto.getId()));

        reply.setContent(dto.getContent());

    }

    // 댓글 및 대댓글 삭제
    @Transactional
    public void deleteReply(Long id){

        Reply reply = replyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + id));

        replyRepository.deleteById(reply);

    }

}