package com.gabesechansoftware.ddapp.models;

import java.io.Serializable;

/**
 * Represents a single store in the feed.  Strips down the large json representation to what
 * is really needed
 */
public class FeedStore implements Serializable {
    private final String tags;
    private final String coverImgUrl;
    private final String name;
    private final String url;
    private final int maxMinutes;
    private final boolean isOpen;
    private final int id;
    private final String headerImgUrl;
    private final int numRatings;
    private final double averageRatings;

    public FeedStore(String name, String tags, String coverImgUrl, String headerImgUrl,
                     int maxMinutes, boolean isOpen,
                     String url, int id, double averageRatings, int numRatings) {
        this.name = name;
        this.tags = tags;
        this.coverImgUrl = coverImgUrl;
        this.maxMinutes = maxMinutes;
        this.isOpen = isOpen;
        this.url = url;
        this.id = id;
        this.headerImgUrl = headerImgUrl;
        this.numRatings = numRatings;
        this.averageRatings = averageRatings;
    }

    public String getHeaderImgUrl() {
        return headerImgUrl;
    }

    public String getTags() {
        return tags;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getMaxMinutes() {
        return maxMinutes;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public int getId() {
        return id;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public double getAverageRatings() {
        return averageRatings;
    }
}
