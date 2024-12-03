package com.example.addmusic.Service;

import com.example.addmusic.Dao.musicDao;
import com.example.addmusic.Model.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class musicService {

    private final musicDao musicDao;

    @Autowired
    public musicService(musicDao musicDao) {
        this.musicDao = musicDao;
    }

    public void addMusic(Music music) {
        musicDao.addMusic(music);
    }

    public List<Music> getAllMusics() {
        return musicDao.getAllmusics();
    }

    public void updateMusic(Music music) {
        musicDao.updateMusic(music);
    }

    public void deleteMusic(int id) {
        musicDao.deleteMusic(id);
    }

    private List<Music> musics = new ArrayList<>();

}
