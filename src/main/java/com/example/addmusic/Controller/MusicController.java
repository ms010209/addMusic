package com.example.addmusic.Controller;

import com.example.addmusic.Model.Music;
import com.example.addmusic.Service.musicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${upload.path}") // application.properties에서 업로드 경로 읽기
    private String uploadsDir;

    @Value("${display-path}") // 업로드 파일 접근 경로 읽기
    private String displayPath;

    private final musicService musicService;

    @Autowired
    public MusicController(musicService musicService) {
        this.musicService = musicService;
    }

    // 대시보드 (음악 목록)
    @GetMapping
    public String listMusics(Model model) {
        List<Music> musics = musicService.getAllMusics();
        model.addAttribute("musics", musics);
        return "dashboard"; // dashboard.html 렌더링
    }

    // 특정 음악 상세보기
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

    // 음악 추가
    @PostMapping("/add")
    public String addMusic(@RequestParam("singer") String singer,
                           @RequestParam("title") String title,
                           @RequestParam("mp3File") MultipartFile mp3File,
                           RedirectAttributes redirectAttributes) {
        try {
            // 파일이 저장될 디렉터리 경로 생성
            Path uploadsPath = Paths.get(uploadsDir);
            Files.createDirectories(uploadsPath); // 업로드 디렉터리 생성

            String fileName = mp3File.getOriginalFilename();
            Path filePath = uploadsPath.resolve(fileName); // 경로가 중복되지 않도록 수정

            mp3File.transferTo(filePath.toFile());

            String fileUrl = displayPath + fileName;
            Music music = new Music(0, singer, title, 0, 0, 0, fileUrl);
            musicService.addMusic(music);

            redirectAttributes.addFlashAttribute("message", "노래가 성공적으로 추가되었습니다!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "파일 업로드 실패!");
        }

        return "redirect:/musics";
    }

    // 음악 수정
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

    // 음악 삭제
    @PostMapping("/delete")
    public String deleteMusic(@RequestParam("id") int id) {
        musicService.deleteMusic(id);
        return "redirect:/musics";
    }
}
