package tripong.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResultDto {

    private int code;
    private String message;
    private String field;
    private String kind;
    private String invalidValue;


    public ErrorResultDto(int code, String message){
        this.code = code;
        this.message = message;
    }

    public ErrorResultDto(FieldError fieldError){
        this.field = fieldError.getField();
        this.kind = fieldError.getCode();
        this.invalidValue = Objects.requireNonNull(fieldError.getRejectedValue()).toString();
        this.message = fieldError.getDefaultMessage();
        this.code = 1; //검증에러
    }
}
