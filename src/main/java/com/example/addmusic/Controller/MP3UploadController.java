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
import java.util.UUID;

@Controller
@RequestMapping("/upload")
public class MP3UploadController {

    @Value("${upload.path}") // 업로드 경로 읽기
    private String uploadsDir;

    @Value("${display-path}") // 업로드 파일 접근 경로 읽기
    private String displayPath;

    private final musicService musicService;

    @Autowired
    public MP3UploadController(musicService musicService) {
        this.musicService = musicService;
    }

    // 모든 음악 목록을 보여주는 페이지
    @GetMapping("/all")
    public String getAllMusics(Model model) {
        model.addAttribute("musics", musicService.getAllMusics());
        return "dashboard"; // dashboard.html 렌더링
    }

    // 음악 추가 (파일 업로드 포함)
    @PostMapping("/add")
    public String addMusic(@RequestParam("singer") String singer,
                           @RequestParam("title") String title,
                           @RequestParam("mp3File") MultipartFile mp3File,
                           RedirectAttributes redirectAttributes) {

        // 파일이 없으면 예외 처리
        if (mp3File.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "파일을 선택해 주세요!");
            return "redirect:/upload/all";
        }

        createUploadsDirectory(); // 디렉터리 생성

        String fileName = UUID.randomUUID().toString() + "_" + mp3File.getOriginalFilename();
        Path filePath = Paths.get(uploadsDir).resolve(fileName);

        try {
            // 파일 저장
            mp3File.transferTo(filePath.toFile());

            // 음악 데이터 생성 및 저장
            String fileUrl = displayPath + fileName;
            Music music = new Music(0, singer, title, 0, 0, 0, fileUrl);
            musicService.addMusic(music);

            redirectAttributes.addFlashAttribute("message", "노래가 성공적으로 추가되었습니다!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "파일 업로드 중 오류가 발생했습니다!");
        }

        return "redirect:/upload/all"; // 업로드 완료 후 음악 목록으로 리다이렉트
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
        return "redirect:/upload/all"; // 수정 완료 후 목록으로 리다이렉트
    }

    // 음악 삭제
    @PostMapping("/delete")
    public String deleteMusic(@RequestParam("id") int id) {
        musicService.deleteMusic(id);
        return "redirect:/upload/all"; // 삭제 완료 후 목록으로 리다이렉트
    }

    // 업로드 디렉토리 생성
    private void createUploadsDirectory() {
        Path path = Paths.get(uploadsDir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
