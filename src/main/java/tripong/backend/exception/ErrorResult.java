package tripong.backend.exception;

import lombok.Data;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ErrorResult {

    private List<ErrorResultDto> errors = new ArrayList<>();

    public ErrorResult(int code, String message) {
        ErrorResultDto baseErrorDto = new ErrorResultDto(code, message);
        errors.add(baseErrorDto);
    }


    public ErrorResult(BindingResult bindingResult) {
        this.errors = bindingResult.getFieldErrors().stream()
                .map(e -> new ErrorResultDto(e)).collect(Collectors.toList());
    }
}
