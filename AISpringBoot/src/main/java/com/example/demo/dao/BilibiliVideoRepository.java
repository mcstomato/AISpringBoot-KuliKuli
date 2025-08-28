package com.example.demo.dao;

import com.example.demo.model.BilibiliVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BilibiliVideoRepository extends JpaRepository<BilibiliVideo, Integer> {

    @Query(value = "SELECT * FROM bilibili_videos ORDER BY RAND() LIMIT 8", nativeQuery = true)
    List<BilibiliVideo> findRandomEight();
}



