package com.gabesechansoftware.ddapp.feedfetcher;

import com.gabesechansoftware.ddapp.api.models.RestaurantNearLocationStore;
import com.gabesechansoftware.ddapp.api.models.RestaurantsNearLocationResult;
import com.gabesechansoftware.ddapp.models.FeedStore;

import java.util.List;

/**
 * A cache of previously seen stores.  This code assumes that stores accumulate linearly.
 * This in an interface instead of a concrete class because I may want to play with
 * disk vs memory vs SQL caching
 */
interface FeedCache {
    /**
     * Get the maximum number of stores that can be found
     * @return maximum number of stores, or 0 if the cache is clear
     */
    int getMaxStores();

    /**
     * Gets the current number of stores
     * @return
     */
    int getCurrentStores();

    /**
     * Gets the store in order of feed
     * @param storeNum
     * @return the store at that position in the feed.  Returns null if that store exists but
     * is not yet cached
     */
    FeedStore getStore(int storeNum);

    /**
     * Gets all of the stores
     * @return the list of all stores
     */
    List<FeedStore> getStores();

    /**
     * Updates the cache with new stores and maximum stores.
     * This assumes whatever code is making the API calls will always feed it in order results
     * If results are not fed in order, duplicates or missing items are possible.
     *
     * @param result the result to update based off of
     */
    void addStores(RestaurantsNearLocationResult result);

    void clearStore();

}
