package com.gabesechansoftware.ddapp.feedfetcher;


import com.gabesechansoftware.ddapp.TestUtils;
import com.gabesechansoftware.ddapp.api.DoorDashApiController;
import com.gabesechansoftware.ddapp.api.models.RestaurantsNearLocationResult;

import org.junit.Test;

import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class FeedFetcherTest {

    @Test
    public void testPrecacheTriggersObservable() throws Exception{
        DoorDashApiController doorDashApiController = mock(DoorDashApiController.class);
        RestaurantsNearLocationResult results =
                TestUtils.createResultWithStores(10, 5);
        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(4);
            callback.onResponse(null, Response.success(results));
            return null;
        }).when(doorDashApiController).getFeed(anyDouble(),
                anyDouble(), anyInt(), anyInt(), any(Callback.class));
        FeedFetcher fetcher = new FeedFetcher(doorDashApiController);
        fetcher.getObservable().subscribe(feed-> {
            assertFalse(feed.isFullyFetched());
            assertEquals(feed.getNumStores(), 5);
            for(int i = 0; i < feed.getNumStores(); i++) {
                assertEquals(results.stores.get(i), feed.getStores().get(i));
            }
            assertEquals(feed, fetcher.getLastFeed());
        });
        fetcher.precache();
    }


    @Test
    public void testDoublePrecache() throws Exception{
        DoorDashApiController doorDashApiController = mock(DoorDashApiController.class);
        RestaurantsNearLocationResult results =
                TestUtils.createResultWithStores(10, 5);
        RestaurantsNearLocationResult results2 =
                TestUtils.createResultWithStores(10, 5);
        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(4);
            callback.onResponse(null, Response.success(results));
            return null;
        }).when(doorDashApiController).getFeed(anyDouble(),
                anyDouble(), anyInt(), anyInt(), any(Callback.class));
        FeedFetcher fetcher = new FeedFetcher(doorDashApiController);
        fetcher.precache();

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(4);
            callback.onResponse(null, Response.success(results2));
            return null;
        }).when(doorDashApiController).getFeed(anyDouble(),
                anyDouble(), anyInt(), anyInt(), any(Callback.class));

        fetcher.getObservable().subscribe(feed-> {
            assertTrue(feed.isFullyFetched());
            assertEquals(feed.getNumStores(), 10);
            for(int i = 0; i < 5; i++) {
                assertEquals(results.stores.get(i), feed.getStores().get(i));
            }
            for(int i = 0; i < 5; i++) {
                assertEquals(results2.stores.get(i), feed.getStores().get(i+5));
            }
            assertEquals(feed, fetcher.getLastFeed());

        });
        fetcher.precache();
    }

    @Test
    public void testResetTriggersObservableTwoTimes() throws Exception{
        DoorDashApiController doorDashApiController = mock(DoorDashApiController.class);
        RestaurantsNearLocationResult results =
                TestUtils.createResultWithStores(10, 5);
        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(4);
            callback.onResponse(null, Response.success(results));
            return null;
        }).when(doorDashApiController).getFeed(anyDouble(),
                anyDouble(), anyInt(), anyInt(), any(Callback.class));
        FeedFetcher fetcher = new FeedFetcher(doorDashApiController);
        boolean valuesFound[] = new boolean[2];
        fetcher.getObservable().subscribe(feed-> {
            if(valuesFound[0] == false) {
                //The first time we trigger, we're empty
                assertFalse(feed.isFullyFetched());
                assertEquals(0, feed.getNumStores());
                assertEquals(0, feed.getStores().size());
                valuesFound[0] = true;
            }
            else {
                //The second time, we have data
                assertFalse(feed.isFullyFetched());
                assertEquals(feed.getNumStores(), 5);
                valuesFound[1] = true;
            }
        });
        fetcher.resetLatLng(0, 0);
        //Ensure we saw both values.  There may be a better way to do this with a TestSubscriber
        assertTrue(valuesFound[0]);
        assertTrue(valuesFound[1]);
    }


}
