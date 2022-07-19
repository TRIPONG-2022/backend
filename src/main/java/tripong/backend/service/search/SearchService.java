package tripong.backend.service.search;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.dto.post.PostResponseDto;
import tripong.backend.dto.search.SearchType;
import tripong.backend.repository.search.SearchRepository;
import tripong.backend.service.aws.AmazonS3Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;
    private final AmazonS3Service amazonS3Service;

    // Post 기능 담당 좌창화님 코드 바탕으로 작성
    @Transactional
    public List<PostResponseDto> search(SearchType searchType, String keyword, Pageable pageable){
        List<PostResponseDto> postList = searchRepository.search(searchType, keyword, pageable)
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        postList.forEach(postResponseDto -> {
            String fileName = postResponseDto.getThumbnail();
            if (fileName != null) {
                postResponseDto.setThumbnail(amazonS3Service.getFile(fileName));
            }
        });

        return postList;
    }

}
