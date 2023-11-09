package com.music.back.controller;

import com.music.back.model.TextRequest;
import com.music.back.model.dto.MusicDTO;
import com.music.back.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/page")
public class PageController {
    private int result;

    private final MusicService musicService;

    @Autowired
    public PageController(MusicService musicService){
        this.musicService = musicService;
    }

    @GetMapping("/intro")
    public String showIntro() {
        return "page/intro";
    }

    @GetMapping("/story")
    public String showStory() {
        return "page/story";
    }


    @PostMapping("/submit")
    public String processStoryForm(@RequestParam("input") String input) {

        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://127.0.0.1:2000/predict/positiveOrNegative";
        TextRequest textRequest = new TextRequest();
        textRequest.setText(input);
        String response = restTemplate.postForObject(apiUrl, textRequest, String.class);

        assert response != null;
        response = response.replaceAll("\"","");

        if(response.equals("Negative")){
            apiUrl = "http://127.0.0.1:2000/predict/sadOrMad";
            response = restTemplate.postForObject(apiUrl, textRequest, String.class);

            assert response != null;
            response = response.replaceAll("\"","");
            if(response.equals("Sad")){
                result = 2;
            } else{
                result = 3;
            }
        } else{
            result = 1;
        }

        return "redirect:/page/result";
    }

    @GetMapping("/result")
    public String showResult(Model model){
        MusicDTO music = musicService.findRandomMusicAboutEmotion(result);
        model.addAttribute("song", music.getSong());
        model.addAttribute("singer", music.getSinger());
        model.addAttribute("emotion", result);
        return "/page/result";
    }

}
