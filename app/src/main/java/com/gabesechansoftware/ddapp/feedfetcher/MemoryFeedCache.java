package com.gabesechansoftware.ddapp.feedfetcher;

import com.gabesechansoftware.ddapp.api.models.RestaurantNearLocationStore;
import com.gabesechansoftware.ddapp.api.models.RestaurantsNearLocationResult;
import com.gabesechansoftware.ddapp.models.FeedStore;

import java.util.ArrayList;
import java.util.List;

/**
 * An in memory implementation of the feed cache.
 */
class MemoryFeedCache implements FeedCache {
    private int maxStores;

    final private List<FeedStore> stores = new ArrayList<>();

    /**
     * Get the maximum number of stores that can be found
     * @return maximum number of stores, or 0 if the cache is clear
     */
    public int getMaxStores() {
        return maxStores;
    }

    /**
     * Gets the current number of stores
     * @return
     */
    public int getCurrentStores() {
        return stores.size();
    }

    /**
     * Gets the store in order of feed
     * @param storeNum
     * @return the store at that position in the feed.  Returns null if that store exists but
     * is not yet cached
     */
    public FeedStore getStore(int storeNum) {
        if(storeNum >= maxStores) {
            throw new IndexOutOfBoundsException("Requested store "+storeNum+
                    " max store is "+maxStores);
        }
        return stores.get(storeNum);
    }

    /**
     * Get a list of all the stores.
     * @return all the stores we know of
     */
    public List<FeedStore> getStores() {
        return stores;
    }

    /**
     * Updates the cache with new stores and maximum stores
     * @param result
     */
    public void addStores(RestaurantsNearLocationResult result) {
        maxStores = result.num_results;
        for(RestaurantNearLocationStore newStore : result.stores) {
            stores.add(newStore.toFeedStore());
        }
    }

    public void clearStore() {
        stores.clear();
        maxStores = 0;
    }

}
