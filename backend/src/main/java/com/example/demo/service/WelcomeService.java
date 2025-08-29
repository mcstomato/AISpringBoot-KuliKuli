package com.example.demo.service;

import com.example.demo.dao.PlaceholderDao;
import org.springframework.stereotype.Service;

@Service
public class WelcomeService {

    private final PlaceholderDao placeholderDao;

    public WelcomeService(PlaceholderDao placeholderDao) {
        this.placeholderDao = placeholderDao;
    }

    public String getWelcomeMessage() {
        return "我是黑大电子1班的田坤，我的学号是20222281";
    }
}


