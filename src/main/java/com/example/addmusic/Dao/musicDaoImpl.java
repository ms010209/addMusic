package com.example.addmusic.Dao;

import com.example.addmusic.Model.Music;
import org.springframework.stereotype.Repository;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

@Repository
public class musicDaoImpl implements musicDao {
    private List<Music> musicList = new ArrayList<>();
    private static int idCounter = 1;

    @Override
    public void addMusic(Music music) {
        music.setId(idCounter++);
        musicList.add(music);
    }

    @Override
    public List<Music> getAllmusics() {
        return musicList;
    }

    @Override
    public void updateMusic(Music music) {
        boolean musicFound = false;
        for (Music m : musicList) {
            if (m.getId() == music.getId()) {
                m.setSinger(music.getSinger());
                m.setTitle(music.getTitle());
                m.setAudioFilePath(music.getAudioFilePath());
                m.setLikes(music.getLikes());
                m.setHearts(music.getHearts());
                m.setViews(music.getViews());
                musicFound = true;
                break;
            }
        }
        if (!musicFound) {
            throw new IllegalArgumentException("No music found with id: " + music.getId());
        }
    }

    @Override
    public void deleteMusic(int id) {
        boolean musicFound = false;
        Iterator<Music> iterator = musicList.iterator();
        while (iterator.hasNext()) {
            Music m = iterator.next();
            if (m.getId() == id) {
                iterator.remove();
                musicFound = true;
                break;
            }
        }
        if (!musicFound) {
            throw new IllegalArgumentException("No music found with id: " + id);
        }
    }
    private void validateMusic(Music music) {
        if (music.getSinger() == null || music.getSinger().trim().isEmpty()) {
            throw new IllegalArgumentException("Singer cannot be null or empty");
        }
        if (music.getTitle() == null || music.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
    }
}
