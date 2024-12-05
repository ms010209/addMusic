package com.example.addmusic.Controller;

import com.example.addmusic.Model.Music;
import com.example.addmusic.Service.musicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.*;

@Controller
@RequestMapping("/musics")
public class MusicController {

    private final musicService musicService;

    @Autowired
    public MusicController(musicService musicService) {
        this.musicService = musicService;
    }

    @GetMapping
    public String getAllMusics(Model model) {
        model.addAttribute("musics", musicService.getAllMusics());
        return "index";
    }

    @PostMapping("/add")
    public String addMusic(@RequestParam("singer") String singer,
                           @RequestParam("title") String title,
                           @RequestParam("mp3File") MultipartFile mp3File,
                           RedirectAttributes redirectAttributes) {
        String fileName = mp3File.getOriginalFilename();
        try {
            Path path = Paths.get("/uploads/" + fileName);
            Files.write(path, mp3File.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Music music = new Music(0, singer, title, 0, 0, 0, "/uploads/" + fileName);
        musicService.addMusic(music);
        return "redirect:/musics";
    }

    @PostMapping("/update")
    public String updateMusic(@RequestParam("id") int id,
                              @RequestParam("singer") String singer,
                              @RequestParam("title") String title,
                              @RequestParam("likes") int likes,
                              @RequestParam("hearts") int hearts,
                              @RequestParam("views") int views) {
        // Music 객체 생성 시 누락된 값들 포함
        Music music = new Music(id, singer, title, likes, hearts, views, "");
        musicService.updateMusic(music);
        return "redirect:/musics";
    }

    @PostMapping("/delete")
    public String deleteMusic(@RequestParam("id") int id) {
        musicService.deleteMusic(id);
        return "redirect:/musics";
    }

    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(MissingServletRequestParameterException.class)
        public String handleMissingParams(MissingServletRequestParameterException ex, RedirectAttributes redirectAttributes) {
            String paramName = ex.getParameterName();
            redirectAttributes.addFlashAttribute("error", paramName + " 파라미터가 누락되었습니다.");
            return "redirect:/musics";
        }
    }
}
