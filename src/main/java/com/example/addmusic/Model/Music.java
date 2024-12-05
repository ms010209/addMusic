package com.example.addmusic.Model;

public class Music {
    private int id;
    private String singer;
    private String title;
    private int likes;
    private int hearts;
    private int views;
    private String filePath;

    public Music(int id, String singer, String title, int likes, int hearts, int views, String filePath) {
        this.id = id;
        this.singer = singer;
        this.title = title;
        this.likes = likes;
        this.hearts = hearts;
        this.views = views;
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getAudioFilePath() {
        return filePath;
    }

    public void setAudioFilePath(String audioFilePath) {
        this.filePath = audioFilePath;
    }
}
