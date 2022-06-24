package tripong.backend.service.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tripong.backend.dto.post.PostRequestDto;
import tripong.backend.dto.post.PostResponseDto;
import tripong.backend.entity.post.Category;
import tripong.backend.entity.post.Post;
import tripong.backend.entity.post.User;
import tripong.backend.repository.post.PostRepository;
import tripong.backend.repository.post.UserRepository;
import tripong.backend.service.post.aws.AmazonS3Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AmazonS3Service amazonS3Service;
    private final ObjectMapper objectMapper;

    @Transactional
    public List<PostResponseDto> findByCategory(Category category, Pageable pageable) {
        List<PostResponseDto> postResponseDtoList = postRepository.findByCategory(category, pageable)
                                                            .stream()
                                                            .map(PostResponseDto::new)
                                                            .collect(Collectors.toList());

        postResponseDtoList.forEach(postResponseDto -> postResponseDto.setThumbnail(amazonS3Service.getFile(postResponseDto.getThumbnail())));
        return postResponseDtoList;
    }
    @Transactional
    public Post save(PostRequestDto requestDto) {
        List<String> imageUrlList = new ArrayList<>();
        requestDto.getImages().forEach(file -> imageUrlList.add(amazonS3Service.uploadFile(file)));
        String thumbnailUrl = amazonS3Service.uploadFile(requestDto.getThumbnail());
        Optional<User> author = userRepository.findById(requestDto.getAuthor());

        Post post = requestDto.toEntity();
        
        /* author, images, thumbnail 별도 처리 */
        post.setAuthor(author.orElseThrow());
        post.setImages(imageUrlList);
        post.setThumbnail(thumbnailUrl);

        return postRepository.save(post);
    }

    @Transactional
    public PostResponseDto findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        PostResponseDto postResponseDto = new PostResponseDto(post);

        List<String> images = new ArrayList<>();
        post.getImages()
                .forEach(fileName -> {
                    String image = amazonS3Service.getFile(fileName);
                    images.add(image);
                });
        postResponseDto.setImages(images);
        String thumbnail = amazonS3Service.getFile(post.getThumbnail());
        postResponseDto.setThumbnail(thumbnail);

        return postResponseDto;
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        post.getImages().forEach(fileName -> amazonS3Service.deleteFile(fileName));
        amazonS3Service.deleteFile(post.getThumbnail());
        postRepository.delete(post);
    }

}
