package com.example.addmusic.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    // 로그인 페이지를 보여주는 GET 메서드
    @GetMapping("/")
    public String login() {
        return "index.html";
    }

    // 로그인 처리를 위한 POST 메서드
    @PostMapping("/")
    public String handleLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              RedirectAttributes redirectAttributes) {

        // 예시로 간단한 로그인 검증 (여기서는 실제 사용자 검증을 하지 않음)
        if ("admin".equals(username) && "password".equals(password)) {
            redirectAttributes.addFlashAttribute("message", "로그인 성공!");
            return "redirect:/musics";  // 로그인 성공 후 /musics 페이지로 리다이렉트
        } else {
            redirectAttributes.addFlashAttribute("message", "로그인 실패! 아이디나 비밀번호를 확인해주세요.");
            return "redirect:login";  // 로그인 실패 시 다시 로그인 페이지로 리다이렉트
        }
    }
}
