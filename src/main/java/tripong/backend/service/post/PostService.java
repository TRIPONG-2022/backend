package tripong.backend.service.post;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tripong.backend.dto.post.PostRequestDto;
import tripong.backend.dto.post.PostResponseDto;
import tripong.backend.entity.post.Category;
import tripong.backend.entity.post.GatheringUser;
import tripong.backend.entity.post.Post;
import tripong.backend.entity.post.PostLike;
import tripong.backend.entity.user.User;
import tripong.backend.exception.post.PostErrorMessage;
import tripong.backend.repository.post.GatheringUserRepository;
import tripong.backend.repository.post.PostLikeRepository;
import tripong.backend.repository.post.PostRepository;
import tripong.backend.repository.user.UserRepository;
import tripong.backend.service.aws.AmazonS3Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    @Value("${cloud.aws.cloudFront.domain}")
    private String domain;

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final PostLikeRepository postLikeRepository;

    private final GatheringUserRepository gatheringUserRepository;

    private final AmazonS3Service amazonS3Service;

    public List<PostResponseDto> findAll(Pageable pageable) {
        List<PostResponseDto> postResponseDtoList =
                postRepository.findAll(pageable)
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
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException(PostErrorMessage.POST_ID_NOT_MATCH.name()));
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
        post.setAuthor(author.orElseThrow(() -> new NoSuchElementException(PostErrorMessage.USER_ID_NOT_MATCH.name())));

        requestDto.getImages().forEach(file -> {
            if (file.getSize() != 0) {
                imagefileNameList.add(amazonS3Service.uploadFile(file));
            }
        });
        post.setImages(imagefileNameList);

        MultipartFile thumbnail = requestDto.getThumbnail();
        if (thumbnail != null && thumbnail.getSize() != 0){
            String fileName = amazonS3Service.uploadFile(thumbnail);
            post.setThumbnail(fileName);
        }

        return postRepository.save(post);
    }

    @Transactional
    @CacheEvict(value="post", key = "#postId")
    public void update(Long postId, PostRequestDto postRequestDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException(PostErrorMessage.POST_ID_NOT_MATCH.name()));

        List<String> imagefileNameList = new ArrayList<>();
        postRequestDto.getImages().forEach(fileName -> imagefileNameList.add(amazonS3Service.uploadFile(fileName)));

        MultipartFile thumbnail = postRequestDto.getThumbnail();
        String thumbnailFileName = null;
        if (thumbnail != null && thumbnail.getSize() != 0){
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
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException(PostErrorMessage.POST_ID_NOT_MATCH.name()));
        post.getImages().forEach(fileName -> amazonS3Service.deleteFile(fileName));
        String fileName = post.getThumbnail();
        if (fileName != null){
            amazonS3Service.deleteFile(fileName);
        }
        postRepository.delete(post);
    }

    @Transactional
    public void updateViewCount(Long postId, Long newViewCount) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException(PostErrorMessage.POST_ID_NOT_MATCH.name()));
        post.setViewCount(newViewCount + post.getViewCount());
    }

    @Transactional
    public void saveLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException(PostErrorMessage.POST_ID_NOT_MATCH.name()));
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(PostErrorMessage.USER_ID_NOT_MATCH.name()));
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
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException(PostErrorMessage.POST_ID_NOT_MATCH.name()));
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(PostErrorMessage.USER_ID_NOT_MATCH.name()));
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
                postRepository.findByIdAndCategoryAndCreatedDate(userId, category.name(), fromDate, endDate, pageable)
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

    public List<PostResponseDto> getPersonalLikePostList(Long userId, Category category, Pageable pageable) {
        List<PostResponseDto> postResponseDtoList =
                postRepository.findLikePostByIdAndCategory(userId, category.name(), pageable)
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

    public String putS3Image(MultipartFile file) {
        String imageUrl = null;

        if (!file.isEmpty()){
            String fileName = amazonS3Service.uploadFile(file);
            imageUrl = domain + "/" + fileName;
        }

        return imageUrl;
    }

}
