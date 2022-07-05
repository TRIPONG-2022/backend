package tripong.backend.service.post;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import tripong.backend.dto.post.PostRequestDto;
import tripong.backend.dto.post.PostResponseDto;
import tripong.backend.entity.post.*;
import tripong.backend.repository.post.GatheringUserRepository;
import tripong.backend.repository.post.PostLikeRepository;
import tripong.backend.repository.post.PostRepository;
import tripong.backend.repository.post.UserRepository;
import tripong.backend.service.aws.AmazonS3Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final PostLikeRepository postLikeRepository;

    private final GatheringUserRepository gatheringUserRepository;

    private final AmazonS3Service amazonS3Service;

    public List<PostResponseDto> findByCategory(Category category, Pageable pageable) {
        List<PostResponseDto> postResponseDtoList =
                postRepository.findByCategory(category, pageable)
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        postResponseDtoList.forEach(postResponseDto -> {
            String fileName = postResponseDto.getThumbnail();
            if (fileName != null) {
                postResponseDto.setThumbnail(amazonS3Service.getFile(fileName));
            }
        });
        return postResponseDtoList;
    }

    @Cacheable(value="post", key = "#postId")
    public PostResponseDto findById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. postId=" + postId));
        PostResponseDto postResponseDto = new PostResponseDto(post);

        List<String> images = new ArrayList<>();
        post.getImages()
                .forEach(fileName -> {
                    String image = amazonS3Service.getFile(fileName);
                    images.add(image);
                });
        postResponseDto.setImages(images);
        String fileName = post.getThumbnail();
        if (fileName != null) {
            String thumbnail = amazonS3Service.getFile(fileName);
            postResponseDto.setThumbnail(thumbnail);
        }

        return postResponseDto;
    }

    @Transactional
    public Post save(PostRequestDto requestDto) {
        List<String> imagefileNameList = new ArrayList<>();

        Post post = requestDto.toEntity();

        /* author, images, thumbnail 별도 처리 */
        Optional<User> author = userRepository.findById(requestDto.getAuthor());
        post.setAuthor(author.orElseThrow());

        requestDto.getImages().forEach(file -> {
            if (file.getSize() != 0) {
                imagefileNameList.add(amazonS3Service.uploadFile(file));
            }
        });
        post.setImages(imagefileNameList);

        MultipartFile thumbnail = requestDto.getThumbnail();
        if (thumbnail.getSize() != 0){
            String fileName = amazonS3Service.uploadFile(thumbnail);
            post.setThumbnail(fileName);
        }

        return postRepository.save(post);
    }

    @Transactional
    @CacheEvict(value="post", key = "#postId")
    public void update(Long postId, PostRequestDto postRequestDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postId));
        List<String> imagefileNameList = new ArrayList<>();
        postRequestDto.getImages().forEach(fileName -> imagefileNameList.add(amazonS3Service.uploadFile(fileName)));
        MultipartFile thumbnail = postRequestDto.getThumbnail();
        String thumbnailFileName = null;
        if (thumbnail.getSize() != 0){
            thumbnailFileName = amazonS3Service.uploadFile(postRequestDto.getThumbnail());
        }
        /* 장애 예방을 위해 delete를 나중에 수행 */
        post.getImages().forEach(fileName -> amazonS3Service.deleteFile(fileName));
        String fileName = post.getThumbnail();
        if (fileName != null) {
            amazonS3Service.deleteFile(fileName);
        }
        post.update(postRequestDto, imagefileNameList, thumbnailFileName);
    }

    @Transactional
    @CacheEvict(value = "post", key = "#postId")
    public void delete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. postId=" + postId));
        post.getImages().forEach(fileName -> amazonS3Service.deleteFile(fileName));
        String fileName = post.getThumbnail();
        if (fileName != null){
            amazonS3Service.deleteFile(fileName);
        }
        postRepository.delete(post);
    }

    @Transactional
    public void updateViewCount(Long postId, Long newViewCount) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. postId=" + postId));
        post.setViewCount(newViewCount + post.getViewCount());
    }

    @Transactional
    public void saveLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. postId=" + postId));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. userId=" + userId));
        Optional<PostLike> postLike = Optional.ofNullable(postLikeRepository.findByPostIdAndUserId(postId, userId));
        if (postLike.isEmpty()) {
            postLikeRepository.save(PostLike.builder()
                    .post(post)
                    .user(user)
                    .build());
        }
    }

    @Transactional
    public void deleteLike(Long postId, Long userId) {
        Optional<PostLike> postLike = Optional.ofNullable(postLikeRepository.findByPostIdAndUserId(postId, userId));
        if (postLike.isPresent()) {
            postLikeRepository.delete(postLike.get());
        }
    }

    @Transactional
    public void saveGatheringUser(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. postId=" + postId));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. userId=" + userId));
        Optional<GatheringUser> gatheringUser = Optional.ofNullable(gatheringUserRepository.findByPostIdAndUserId(postId, userId));
        if (gatheringUser.isEmpty()) {
            gatheringUserRepository.save(GatheringUser.builder()
                    .post(post)
                    .user(user)
                    .build());
        }
    }

    @Transactional
    public void deleteGatheringUser(Long postId, Long userId) {
        Optional<GatheringUser> gatheringUser = Optional.ofNullable(gatheringUserRepository.findByPostIdAndUserId(postId, userId));
        if (gatheringUser.isPresent()) {
            gatheringUserRepository.delete(gatheringUser.get());
        }
    }

    public List<PostResponseDto> getPersonalPostList(Long userId, Category category, LocalDate fromDate, LocalDate endDate, Pageable pageable) {
        List<PostResponseDto> postResponseDtoList =
                postRepository.findByIdAndCategory(userId, category.name(), fromDate, endDate, pageable)
                        .stream()
                        .map(PostResponseDto::new)
                        .collect(Collectors.toList());

        postResponseDtoList.forEach(postResponseDto -> {
            String fileName = postResponseDto.getThumbnail();
            if (fileName != null) {
                postResponseDto.setThumbnail(amazonS3Service.getFile(fileName));
            }
        });
        return postResponseDtoList;
    }

}
