package com.example.addmusic.Controller;


import com.example.addmusic.Model.User;
import com.example.addmusic.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignupController {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("message", "비밀번호가 일치하지 않습니다.");
            return "signup";
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // 새로운 사용자 객체 생성
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        // 사용자 저장
        userRepository.save(user);

        model.addAttribute("message", "회원가입 성공!");
        return "index";
    }
}