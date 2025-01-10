package com.example.addmusic.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    // 로그인 페이지
    @GetMapping("/")
    public String login() {
        return "index";
    }

    // 로그인 처리
    @PostMapping("/")
    public String handleLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              RedirectAttributes redirectAttributes) {

        if ("admin".equals(username) && "password".equals(password)) {
            redirectAttributes.addFlashAttribute("message", "로그인 성공!");
            return "redirect:/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("message", "로그인 실패! 아이디나 비밀번호를 확인해주세요.");
            return "redirect:/";
        }
    }

    // 대시보드 페이지
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signup(Model model) {
        return "signup"; // signup.html을 반환
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String handleSignup(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("confirmPassword") String confirmPassword,
                               RedirectAttributes redirectAttributes) {

        // 비밀번호와 비밀번호 확인이 일치하는지 확인
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("message", "비밀번호가 일치하지 않습니다.");
            return "redirect:/signup";
        }

        // 이미 존재하는 사용자 처리 (예시로 'admin' 사용자가 이미 존재한다고 가정)
        if ("admin".equals(username)) {
            redirectAttributes.addFlashAttribute("message", "이미 존재하는 아이디입니다.");
            return "redirect:/signup";
        }

        // 회원가입 성공 처리
        redirectAttributes.addFlashAttribute("message", "회원가입 성공!");
        return "redirect:/";
    }
}
