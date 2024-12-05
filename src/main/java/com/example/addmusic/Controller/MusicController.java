package com.example.addmusic.Controller;

import com.example.addmusic.Model.Music;
import com.example.addmusic.Service.musicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/musics")
public class MusicController {

    private final musicService musicService;

    @Autowired
    public MusicController(musicService musicService) {
        this.musicService = musicService;
    }

    @GetMapping
    public String listMusics(Model model) {
        List<Music> musics = musicService.getAllMusics();
        model.addAttribute("musics", musics);
        return "dashoboard.html"; // index
    }

    @GetMapping("/{id}")
    public String getMusic(@PathVariable("id") int id, Model model) {
        Music music = musicService.getAllMusics().stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);

        if (music != null) {
            model.addAttribute("music", music);
            return "music-detail";
        } else {
            return "redirect:/musics";
        }
    }

    @PostMapping("/add")
    public String addMusic(@RequestParam("singer") String singer,
                           @RequestParam("title") String title,
                           @RequestParam("mp3File") MultipartFile mp3File,
                           RedirectAttributes redirectAttributes) {
        try {
            String fileName = mp3File.getOriginalFilename();
            String uploadsDir = "C:/Users/202-1354/IdeaProjects/addMusic/uploads/";

            Path path = Paths.get(uploadsDir + fileName);
            Files.createDirectories(path.getParent());

            mp3File.transferTo(path.toFile());

            String filePath = "/uploads/" + fileName;
            Music music = new Music(0, singer, title, 0, 0, 0, filePath);
            musicService.addMusic(music);


            redirectAttributes.addFlashAttribute("message", "노래가 성공적으로 추가되었습니다!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "파일 업로드 실패!");
        }

        return "redirect:/musics";
    }

    @PostMapping("/update")
    public String updateMusic(@RequestParam("id") int id,
                              @RequestParam("singer") String singer,
                              @RequestParam("title") String title,
                              @RequestParam("likes") int likes,
                              @RequestParam("hearts") int hearts,
                              @RequestParam("views") int views,
                              @RequestParam("audioFilePath") String audioFilePath) {
        Music music = new Music(id, singer, title, likes, hearts, views, audioFilePath);
        musicService.updateMusic(music);
        return "redirect:/musics";
    }

    @PostMapping("/delete")
    public String deleteMusic(@RequestParam("id") int id) {
        musicService.deleteMusic(id);
        return "redirect:/musics";
    }
}