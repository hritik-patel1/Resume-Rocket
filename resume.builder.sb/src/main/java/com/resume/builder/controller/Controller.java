package com.resume.builder.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generate")
@AllArgsConstructor
public class Controller {
    @GetMapping("/fin")
    public String generateContent(){
        return "Hey baby";
    }
}
