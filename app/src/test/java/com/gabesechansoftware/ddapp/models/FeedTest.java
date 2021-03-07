package com.gabesechansoftware.ddapp.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
public class FeedTest {

    @Test
    public void testStoresIsCopy() {
        List<FeedStore> stores = new ArrayList<>();
        Feed feed = new Feed(stores, true);
        assertNotNull(feed.getStores());
        assertNotSame(stores, feed.getStores());
    }
}
