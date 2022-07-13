package tripong.backend.dto.search;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchRequestDto {
    String keyword;
    SearchType type;

    public SearchRequestDto(String keyword, SearchType type){
        this.keyword = keyword;
        this.type = type;
    }
}
