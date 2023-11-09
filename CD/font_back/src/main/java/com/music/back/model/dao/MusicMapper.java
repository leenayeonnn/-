package com.music.back.model.dao;

import com.music.back.model.dto.MusicDTO;

public interface MusicMapper {
    MusicDTO findRandomMusicAboutEmotion(int result);
}
