package tripong.backend.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tripong.backend.entity.post.Post;
import tripong.backend.service.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TestController {

    private final UserService userService;

    @GetMapping("/home123")
    @ResponseBody
    public String home(){

        userService.test1();

        return "home";
    }




}