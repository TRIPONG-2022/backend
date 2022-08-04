package tripong.backend.controller.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tripong.backend.dto.post.PostResponseDto;
import tripong.backend.dto.search.SearchType;
import tripong.backend.service.search.SearchService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchApiController {

    private final SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam("searchType") SearchType searchType, @RequestParam("keyword") String keyword, Pageable pageable){
        List<PostResponseDto> postList = searchService.search(searchType, keyword, pageable);

        log.info("(" + keyword + ") 키워드 검색 완료");
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }
}
