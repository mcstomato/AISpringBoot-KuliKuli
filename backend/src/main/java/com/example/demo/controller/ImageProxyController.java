package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

@RestController
public class ImageProxyController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/proxy/image")
    public ResponseEntity<byte[]> proxyImage(@RequestParam("url") String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (!(imageUrl.startsWith("http://") || imageUrl.startsWith("https://"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.REFERER, "https://www.bilibili.com/");
            headers.add(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");
            RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, new URI(imageUrl));
            ResponseEntity<byte[]> upstream = restTemplate.exchange(request, byte[].class);

            MediaType contentType = upstream.getHeaders().getContentType();
            HttpHeaders respHeaders = new HttpHeaders();
            if (contentType != null) {
                respHeaders.setContentType(contentType);
            } else {
                respHeaders.setContentType(MediaType.IMAGE_JPEG);
            }
            // 允许跨域加载图片
            respHeaders.add("Access-Control-Allow-Origin", "*");

            return new ResponseEntity<>(upstream.getBody(), respHeaders, upstream.getStatusCode());
        } catch (URISyntaxException | RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(("Proxy error: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        }
    }
}


