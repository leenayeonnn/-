package com.music.back.service;

import com.music.back.model.dao.MusicMapper;
import com.music.back.model.dto.MusicDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MusicService {

    private final MusicMapper musicMapper;


    public MusicService(MusicMapper musicMapper){
        this.musicMapper = musicMapper;
    }
    public MusicDTO findRandomMusicAboutEmotion(int result) {

        return musicMapper.findRandomMusicAboutEmotion(result);

    }
}
