package com.music.back.model.dto;

public class MusicDTO {

    private String song;
    private String singer;

    public MusicDTO() {
    }

    public MusicDTO(String song, String singer) {
        this.song = song;
        this.singer = singer;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }
}
