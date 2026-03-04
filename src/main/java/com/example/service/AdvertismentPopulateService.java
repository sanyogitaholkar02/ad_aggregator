package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Ads;
import com.example.pojo.Advertisments;
import com.example.repo.AdvertismentRepo;

@Service
public class AdvertismentPopulateService {

    @Autowired
    AdvertismentRepo repo;

    public List<Advertisments> getListAdsForUser(String userid) {
        return List.of(new Advertisments("1", "https://google.com", "Google", "https://google.com/image", "Google Ad",
                "Technology", "Technology", "Google", userid));
    }

    public Advertisments getAdsForUser(String userid) {
        return new Advertisments("1", "https://google.com", "Google", "https://google.com/image", "Google Ad",
                "Technology", "Technology", "Google", userid);
    }

    // Not a feasible method
    public List<Advertisments> getAllAds() {
        List<Ads> ads = repo.findAll();

        List<Advertisments> listAds = new ArrayList<>();
        for (Ads ad : ads) {

            Advertisments advertisments = new Advertisments(ad.getAdID(), ad.getRedirectURL(), ad.getCampaignId(),
                    ad.getCompany(), null, null, null, null, null);
            listAds.add(advertisments);

        }
        return listAds;
    }

    @Transactional
    public List<Ads> addAdsList(List<Advertisments> advertismentsList) {
        System.out.println("here");
        List<Ads> adsList = new ArrayList<>();
        for (Advertisments a : advertismentsList) {
            adsList.add(new Ads(a.getAdID(), a.getRedirectURL(), a.getCampaignId(), a.getCompany()));
        }
        return repo.saveAll(adsList);
    }

}
