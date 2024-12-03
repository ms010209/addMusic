package com.example.addmusic.Controller;

import com.example.addmusic.Model.Music;
import com.example.addmusic.Service.musicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                           @RequestParam("title") String title) {
        Music music = new Music(0, singer, title, 0, 0, 0);
        musicService.addMusic(music);
        return "redirect:/musics";
    }

    @PostMapping("/update")
    public String updateMusic(@RequestParam("id") int id,
                              @RequestParam("likes") int likes,
                              @RequestParam("hearts") int hearts,
                              @RequestParam("views") int views) {
        Music music = new Music(id, "", "", likes, hearts, views);
        musicService.updateMusic(music);
        return "redirect:/musics";
    }

    @PostMapping("/delete")
    public String deleteMusic(@RequestParam("id") int id) {
        musicService.deleteMusic(id);
        return "redirect:/musics";
    }

}
