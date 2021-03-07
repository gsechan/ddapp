package com.gabesechansoftware.ddapp.api.models;

import com.gabesechansoftware.ddapp.models.FeedStore;

import java.util.Collections;

public class RestaurantNearLocationStore {
    public int id;
    public String description;
    public String cover_img_url;
    public String name;
    public String url;
    public RestaurantStatus status;

    public FeedStore toFeedStore() {
        int maxMinutes = 0;
        boolean isOpen = false;
        if (status != null) {
            isOpen = status.unavailable_reason == null;
            maxMinutes = status.asap_minutes_range.isEmpty() ? 100 : Collections.max(status.asap_minutes_range);
        }
        return new FeedStore(name, description, cover_img_url, maxMinutes, isOpen, url);
    }
}
