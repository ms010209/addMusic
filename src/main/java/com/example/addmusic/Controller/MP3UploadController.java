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
import java.nio.file.*;

@Controller
@RequestMapping("/upload")
public class MP3UploadController {

    private final musicService musicService;

    @Autowired
    public MP3UploadController(musicService musicService) {
        this.musicService = musicService;
    }

    @GetMapping("/all")
    public String getAllMusics(Model model) {
        model.addAttribute("musics", musicService.getAllMusics());
        return "index";
    }

    @PostMapping("/upload/musics/add")
    public String addMusic(@RequestParam("singer") String singer,
                           @RequestParam("title") String title,
                           @RequestParam("mp3File") MultipartFile mp3File,
                           RedirectAttributes redirectAttributes) {
        String fileName = mp3File.getOriginalFilename();

        try {
            Path path = Paths.get("C:/Users/202-1354/Desktop/uploads/" + fileName);
            Files.write(path, mp3File.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "파일 업로드 실패!");
            return "redirect:/musics";
        }

        String filePath = "/uploads/" + fileName;
        Music music = new Music(0, singer, title, 0, 0, 0, filePath);
        musicService.addMusic(music);


        redirectAttributes.addFlashAttribute("message", "노래가 성공적으로 추가되었습니다!");
        return "redirect:/musics";
    }

    private void createUploadsDirectory() {
        Path path = Paths.get("C:/Users/202-1354/Desktop/uploads");
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping("/add")
    public String addMusic(@RequestParam("singer") String singer,
                           @RequestParam("title") String title,
                           @RequestParam("mp3File") MultipartFile mp3File) {
        createUploadsDirectory();

        String fileName = mp3File.getOriginalFilename();
        try {
            Path filePath = Paths.get("C:/Users/202-1354/Desktop/uploads/" + fileName);
            mp3File.transferTo(filePath.toFile());

            Music music = new Music(0, singer, title, 0, 0, 0, "C:/Users/202-1354/Desktop/uploads/" + fileName);
            musicService.addMusic(music);
        } catch (IOException e) {
            e.printStackTrace();
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
                              @RequestParam("audioFilePath") String audioFilePath) {  // 파일 경로 추가
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
