package com.gabesechansoftware.ddapp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * The Feed data at a point in time.  This should be immutable, new Feed objects will be created
 * when this feed is no longer valid.
 */
public class Feed {
    final private List<FeedStore> stores;
    final boolean isFullyFetched;

    public Feed(List<FeedStore> stores, boolean isFullyFetched) {
        this.stores = new ArrayList<>(stores);
        this.isFullyFetched = isFullyFetched;
    }

    public List<FeedStore> getStores() {
        return stores;
    }

    public int getNumStores() {
        return stores.size();
    }

    public boolean isFullyFetched() {
        return isFullyFetched;
    }
}
