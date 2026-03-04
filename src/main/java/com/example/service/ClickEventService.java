package com.example.service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.entity.Ads;
import com.example.pojo.ClickEventRequest;
import com.example.repo.AdvertismentRepo;
import com.example.repo.ClickEventRepo;

@Service
public class ClickEventService {

    private final ClickEventRepo repository;

    private final AdvertismentRepo repo;

    private final RedisTemplate<String, String> redisTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public static final String TOPIC = "ad-click-topic-1";

    public ClickEventService(RedisTemplate<String, String> redisTemplate,
            KafkaTemplate<String, String> kafkaTemplate, ClickEventRepo repository, AdvertismentRepo repo) {
        this.repository = repository;
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.repo = repo;
    }

    public boolean isDuplicate(String idempotencyKey) {
        // Check if key exists in Redis
        return Boolean.TRUE.equals(redisTemplate.hasKey(idempotencyKey));
    }

    public void markProcessed(String idempotencyKey) {
        // Storing key in Redis for 1 hour to prevent duplicate

        String redisKey = "ad-aggregator:idempotency:" + idempotencyKey;
        Boolean exists = redisTemplate.hasKey(redisKey);
        if (exists == null || !exists) {
            redisTemplate.opsForValue().set(redisKey, "processed", Duration.ofHours(1));
            // process the click event
        } else {
            // click already processed — ignore
        }
    }

    public void produceToKafka(String message) {
        kafkaTemplate.send(
                TOPIC, message);
    }

    public String processClickRequest(ClickEventRequest request) {
        Ads ads = new Ads();
        ads = repo.findByAdID(request.getAdId());

        String redirectUrl = ads.getRedirectURL();

        // Produce click event to Kafka
        String kafkaMessage = String.format(
                "{\"adId\":\"%s\",\"idempotencyKey\":\"%s\",\"timestamp\":\"%s\"}",
                request.getAdId(),
                request.getIdempotencyKey(),
                java.time.Instant.now().toString());

        produceToKafka(kafkaMessage);

        // Mark processed in Redis
        markProcessed(request.getIdempotencyKey());
        return redirectUrl;

    }
    /*
     * First POST → produces Kafka message + 302 redirect
     * 
     * Second POST with same idempotencyKey → skips Kafka but still returns 302
     */

    /*
     * In-memory aggregation counters
     * private final Map<String, AtomicInteger> adClickCounts = new
     * ConcurrentHashMap<>();
     * private final Map<String, AtomicInteger> campaignClickCounts = new
     * ConcurrentHashMap<>();
     * 
     * Limitations:
     * In-memory counters won’t survive app restart
     * Doesn’t handle idempotency
     * Doesn’t scale horizontally (multiple instances will have inconsistent
     * counters)
     * No async processing / Kafka streaming
     */
    // public void trackClick(String adId, String campaignId, String userId, String
    // redirectUrl) {

    // // Validate inputs (basic example)
    // if (adId == null || campaignId == null || redirectUrl == null) {
    // throw new IllegalArgumentException("Invalid click data");
    // }
    // ClickEvent click = new ClickEvent(adId, campaignId, userId, redirectUrl);
    // repository.save(click);

    // // Aggregate click counts in memory
    // adClickCounts.computeIfAbsent(adId, k -> new
    // AtomicInteger()).incrementAndGet();
    // campaignClickCounts.computeIfAbsent(campaignId, k -> new
    // AtomicInteger()).incrementAndGet();

    // System.out.println("Tracked click for ad: " + adId + " redirecting to: " +
    // redirectUrl);
    // }

    // // For reporting
    // public Map<String, Integer> getAdClickCounts() {
    // Map<String, Integer> result = new ConcurrentHashMap<>();
    // adClickCounts.forEach((k, v) -> result.put(k, v.get()));
    // return result;
    // }

    // public Map<String, Integer> getCampaignClickCounts() {
    // Map<String, Integer> result = new ConcurrentHashMap<>();
    // campaignClickCounts.forEach((k, v) -> result.put(k, v.get()));
    // return result;
    // }
}
