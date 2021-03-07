package com.gabesechansoftware.ddapp.models;

/**
 * Represents a single store in the feed.  Strips down the large json representation to what
 * is really needed
 */
public class FeedStore {
    private final String tags;
    private final String coverImgUrl;
    private final String name;
    private final String url;
    private final int maxMinutes;
    private final boolean isOpen;

    public FeedStore(String name, String tags, String coverImgUrl, int maxMinutes, boolean isOpen,
                     String url) {
        this.name = name;
        this.tags = tags;
        this.coverImgUrl = coverImgUrl;
        this.maxMinutes = maxMinutes;
        this.isOpen = isOpen;
        this.url = url;
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
}
