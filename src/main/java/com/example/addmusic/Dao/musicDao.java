package com.example.addmusic.Dao;

import com.example.addmusic.Model.Music;
import java.util.List;

public interface musicDao {
    void addMusic(Music music);
    List<Music> getAllmusics();
    void updateMusic(Music music);
    void deleteMusic(int id);
}
