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
            model.addAttribute("message", "��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
            return "signup";
        }

        // ��й�ȣ ��ȣȭ
        String encodedPassword = passwordEncoder.encode(password);

        // ���ο� ����� ��ü ����
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        // ����� ����
        userRepository.save(user);

        model.addAttribute("message", "ȸ������ ����!");
        return "index";
    }
}