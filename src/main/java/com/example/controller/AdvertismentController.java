package com.example.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Ads;
import com.example.pojo.Advertisments;
import com.example.service.AdvertismentPopulateService;
import com.example.service.S3Service;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/advertisments")
public class AdvertismentController {

    @Autowired
    private AdvertismentPopulateService service;

    @Autowired
    private S3Service serv;

    @PostMapping
    public ResponseEntity<List<Ads>> addAdvertisment(
            @Valid @RequestBody List<@Valid Advertisments> advertismentsList) {
        System.out.println("hello");
        return ResponseEntity.ok(service.addAdsList(advertismentsList));
    }

    @GetMapping
    public List<Advertisments> getAll() {
        return service.getAllAds();
    }

    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadAdImage(@RequestParam("file") MultipartFile file,
            @RequestParam("adId") String adId) throws Exception {

        String key = "ads/" + adId + "-" + file.getOriginalFilename();
        String url = serv.uploadFile(file, key);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/app")
    public String checkApplicationStart() {
        return "Yes!! Application ad-aggregator is running";
    }

    @PostMapping("/add")
    public ResponseEntity<Ads> addAd(@RequestParam("file") MultipartFile file,
            @RequestParam("adId") String adId,
            @RequestParam("campaignId") String campaignId,
            @RequestParam("company") String company,
            @RequestParam("redirectUrl") String redirectUrl) throws IOException {

        String key = "ads/" + adId + "-" + file.getOriginalFilename();
        String imageUrl = serv.uploadFile(file, key);

        Ads ad = null;// new Ads(adId, campaignId, company, redirectUrl, imageUrl);
        // repo.save(ad);

        return ResponseEntity.ok(ad);
    }

}
