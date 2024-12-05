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
        return "index";  // index.html에 모든 음악 목록을 보여줍니다.
    }

    @PostMapping("/upload/musics/add")
    public String addMusic(@RequestParam("singer") String singer,
                           @RequestParam("title") String title,
                           @RequestParam("mp3File") MultipartFile mp3File,
                           RedirectAttributes redirectAttributes) {
        // MP3 파일의 이름을 가져오기
        String fileName = mp3File.getOriginalFilename();

        try {
            // 파일을 저장할 경로
            Path path = Paths.get("/uploads/" + fileName);
            // 파일을 서버의 uploads 폴더에 저장
            Files.write(path, mp3File.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "파일 업로드 실패!");
            return "redirect:/musics";  // 파일 업로드 실패 시 다시 페이지로 이동
        }

        // Music 객체 생성 및 저장
        Music music = new Music(0, singer, title, 0, 0, 0, "/uploads/" + fileName); // audioFilePath를 /uploads/ + fileName으로 설정
        musicService.addMusic(music);

        redirectAttributes.addFlashAttribute("message", "노래가 성공적으로 추가되었습니다!");
        return "redirect:/musics";  // 노래 추가 후 목록 페이지로 리다이렉트
    }

    private void createUploadsDirectory() {
        Path path = Paths.get("uploads");
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
        createUploadsDirectory(); // 업로드 폴더 확인 및 생성

        // 파일 저장
        String fileName = mp3File.getOriginalFilename();
        try {
            Path filePath = Paths.get("/uploads/", fileName);
            mp3File.transferTo(filePath.toFile());

            // 음악 정보 저장
            Music music = new Music(0, singer, title, 0, 0, 0, "/uploads/" + fileName);
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
                              @RequestParam("views") int views) {
        // Music 객체 생성 시 누락된 값들 포함
        Music music = new Music(id, singer, title, likes, hearts, views, ""); // audioFilePath는 필요시 추가
        musicService.updateMusic(music);
        return "redirect:/musics";  // 업데이트 후 목록 페이지로 리다이렉트
    }

    @PostMapping("/delete")
    public String deleteMusic(@RequestParam("id") int id) {
        musicService.deleteMusic(id);
        return "redirect:/musics";  // 삭제 후 목록 페이지로 리다이렉트
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
