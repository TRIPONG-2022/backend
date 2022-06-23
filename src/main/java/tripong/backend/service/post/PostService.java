package tripong.backend.service.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import tripong.backend.dto.post.PostRequestDto;
import tripong.backend.entity.post.Post;
import tripong.backend.entity.post.User;
import tripong.backend.repository.post.PostRepository;
import tripong.backend.repository.post.UserRepository;
import tripong.backend.service.post.aws.AmazonS3Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AmazonS3Service amazonS3Service;
    private final ObjectMapper objectMapper;

    @Transactional
    public Post save(PostRequestDto requestDto) {
        List<String> imageUrlList = new ArrayList<>();
        requestDto.getImages().forEach(file -> imageUrlList.add(amazonS3Service.uploadFile(file)));
        String thumbnailUrl = amazonS3Service.uploadFile(requestDto.getThumbnail());
        Optional<User> author = userRepository.findById(requestDto.getAuthor());

        Post post = Post.builder()
                .author(author.orElseThrow())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .category(requestDto.getCategory())
                .tags(requestDto.getTags())
                .latitude(requestDto.getLatitude())
                .longitude(requestDto.getLongitude())
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .totalHeadCount(requestDto.getTotalHeadCount())
                .images(imageUrlList)
                .thumbnail(thumbnailUrl)
                .budget(requestDto.getBudget())
                .build();

        return postRepository.save(post);
    }

    @Transactional
    public MultiValueMap<String, Object> findById(Long id) {
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        formData.add("postId", post.getId());
        formData.add("author", post.getAuthor().getName());
        formData.add("title", post.getTitle());
        formData.add("content", post.getContent());
        formData.add("category", post.getCategory().toString());
        formData.add("tags", post.getTags());
        formData.add("latitude", post.getLatitude());
        formData.add("longitude", post.getLongitude());
        formData.add("startDate", post.getStartDate());
        formData.add("endDate", post.getEndDate());
        formData.add("curHeadCount", post.getCurHeadCount());
        formData.add("totalHeadCount", post.getTotalHeadCount());
        formData.add("budget", post.getBudget());
        formData.add("recommendationCount", post.getRecommendationCount());
        formData.add("viewCount", post.getViewCount());

        post.getImages()
                .forEach(fileName -> {
                    String image = amazonS3Service.getFile(fileName);
                    formData.add("images",image);
                });
        String thumbnail = amazonS3Service.getFile(post.getThumbnail());
        formData.add("thumbnail",thumbnail);

        return formData;
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        post.getImages().forEach(fileName -> amazonS3Service.deleteFile(fileName));
        amazonS3Service.deleteFile(post.getThumbnail());
        postRepository.delete(post);
    }

}
