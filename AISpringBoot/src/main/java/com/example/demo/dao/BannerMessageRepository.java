package com.example.demo.dao;

import com.example.demo.model.BannerMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerMessageRepository extends JpaRepository<BannerMessage, Long> {
}



