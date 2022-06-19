package tripong.backend.controller.post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostApiController {

    @PostMapping("/gathering")
    public ResponseEntity<Object> saveGathering() {

        return new ResponseEntity(HttpStatus.CREATED);
    }

}
