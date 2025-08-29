package com.example.demo.controller;

import com.example.demo.dao.BannerMessageRepository;
import com.example.demo.model.BannerMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class BannerController {

    private final BannerMessageRepository repository;

    public BannerController(BannerMessageRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/api/banner", produces = "text/plain;charset=UTF-8")
    public String getBannerContent() {
        Optional<BannerMessage> first = repository.findAll().stream().findFirst();
        return first.map(BannerMessage::getContent).orElse("哔哩干杯 (゜-゜)つロ");
    }
}


