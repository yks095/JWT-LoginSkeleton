package me.kiseok.jwtskeleton.domain;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/sign-in")
    public String login()   {
        return "sign-in";
    }

}
