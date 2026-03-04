package com.example.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.example.pojo.Advertisments;
import com.example.pojo.ClickEventRequest;
import com.example.service.AdvertismentPopulateService;
import com.example.service.ClickEventService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/click")
public class ClickEventController {

    @Autowired
    private ClickEventService clickService;

    @Autowired
    private AdvertismentPopulateService adsService;

    ClickEventController(ClickEventService clickService, AdvertismentPopulateService adsService) {
        this.clickService = clickService;
        this.adsService = adsService;
    }

    @PostMapping("adclick")
    public ResponseEntity<?> postMethodName(@Valid @RequestBody ClickEventRequest request) {

        if (clickService.isDuplicate(request.getIdempotencyKey())) {
            // Already processed → return 302 anyway
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", request.getRedirectUrl());
            return ResponseEntity.status(302).headers(headers).build();
        }

        String url = clickService.processClickRequest(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", url);
        return ResponseEntity.status(302).headers(headers).build();
    }

}
