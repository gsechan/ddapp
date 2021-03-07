package com.gabesechansoftware.ddapp.feedfetcher;

import com.gabesechansoftware.ddapp.TestUtils;
import com.gabesechansoftware.ddapp.api.models.RestaurantsNearLocationResult;
import com.gabesechansoftware.ddapp.models.FeedStore;

import org.junit.Test;

import java.util.List;

import static  org.junit.Assert.*;

public class MemoryFeedCacheTest {

    @Test
    public void testMaxStoresSaved() {
        MemoryFeedCache cache = new MemoryFeedCache();
        assertEquals(0, cache.getMaxStores());

        RestaurantsNearLocationResult result = TestUtils.createResultWithStores(100, 10);
        cache.addStores(result);
        assertEquals(100, cache.getMaxStores());

        result = TestUtils.createResultWithStores(200, 10);
        cache.addStores(result);
        assertEquals(200, cache.getMaxStores());
    }

    @Test
    public void testAddingStoresToCurrentStores() {
        MemoryFeedCache cache = new MemoryFeedCache();
        assertEquals(0, cache.getCurrentStores());

        RestaurantsNearLocationResult result = TestUtils.createResultWithStores(100, 10);
        cache.addStores(result);
        assertEquals(10, cache.getCurrentStores());

        result = TestUtils.createResultWithStores(200, 10);
        cache.addStores(result);
        assertEquals(20, cache.getCurrentStores());
    }

    @Test
    public void testFetchingStores() {
        MemoryFeedCache cache = new MemoryFeedCache();
        RestaurantsNearLocationResult result = TestUtils.createResultWithStores(100, 10);
        cache.addStores(result);
        RestaurantsNearLocationResult result2 = TestUtils.createResultWithStores(200, 10);
        cache.addStores(result2);

        List<FeedStore> allStores = cache.getStores();

        for(int i=0; i < 10; i++) {
            assertEquals(result.stores.get(i), cache.getStore(i));
            assertEquals(result.stores.get(i), allStores.get(i));
        }
        for(int i=0; i < 10; i++) {
            assertEquals(result2.stores.get(i), cache.getStore(i + 10));
            assertEquals(result2.stores.get(i), allStores.get(i + 10));
        }

    }

    @Test
    public void testClearingStores() {
        MemoryFeedCache cache = new MemoryFeedCache();
        RestaurantsNearLocationResult result = TestUtils.createResultWithStores(100, 10);
        cache.addStores(result);
        cache.clearStore();

        assertEquals(0, cache.getMaxStores());
        assertEquals(0, cache.getCurrentStores());
    }

}
