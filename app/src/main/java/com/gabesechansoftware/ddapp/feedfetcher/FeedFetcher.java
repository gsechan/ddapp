package com.gabesechansoftware.ddapp.feedfetcher;

import com.gabesechansoftware.ddapp.api.DoorDashApiController;
import com.gabesechansoftware.ddapp.api.models.RestaurantsNearLocationResult;
import com.gabesechansoftware.ddapp.models.Feed;


import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fetches the feed in chunks as requested.  Callers will tell it when they
 * need the next chunk by calling precache.  The observable will update when new data
 * is added.  Intelligently makes requests or not as data is needed.
 *
 * Since this class always fetches every block of data in order and adds it to the cache in order,
 * it will avoid the possible data loss/duplication issues made possible by the FeedCache interface.
 */
public class FeedFetcher {

    private static final int REQUEST_LIMIT = 100;
    private int nextOffset;
    private double lat;
    private double lng;

    final private FeedCache cache = new MemoryFeedCache();

    private boolean isFetching;
    private PublishSubject<Feed> observable = PublishSubject.create();
    final private DoorDashApiController controller;

    private Feed lastFeed;

    public FeedFetcher(DoorDashApiController controller) {
        this.controller = controller;
        lastFeed = new Feed(new ArrayList<>(), false);
    }

    /**
     * Sets a new latitude and longitude for the feed.  Forces a fetch for the new location
     * @param lat latitude
     * @param lng longitude
     */
    public synchronized void resetLatLng(double lat, double lng) {
        //We really ought to compare the new values vs the old ones and not reset if the
        //change is too small to matter.
        this.lat = lat;
        this.lng = lng;
        nextOffset = 0;

        //New location means all the old data is bad.  Clear it and grab a new batch of data
        cache.clearStore();
        lastFeed = new Feed(cache.getStores(), false);
        observable.onNext(lastFeed);
        precache();
    }

    /**
     * Precaches the next chunk of stores
     */
    public synchronized void precache() {
        //avoid double fetching
        if(isFetching) {
            return;
        }
        if(nextOffset >= cache.getMaxStores() && cache.getMaxStores() != 0) { //We've exceeded the max
            return;
        }
        isFetching = true;
        controller.getFeed(lat, lng, nextOffset, REQUEST_LIMIT, new Callback<RestaurantsNearLocationResult>() {
            @Override
            public void onResponse(Call<RestaurantsNearLocationResult> call,
                                   Response<RestaurantsNearLocationResult> response) {
                synchronized (FeedFetcher.this) {
                    if (response.isSuccessful()) {
                        cache.addStores(response.body());
                        nextOffset = cache.getCurrentStores();
                        lastFeed = new Feed(cache.getStores(),
                                cache.getCurrentStores() == cache.getMaxStores());
                        observable.onNext(lastFeed);
                    }
                    isFetching = false;
                }
            }

            @Override
            public void onFailure(Call<RestaurantsNearLocationResult> call, Throwable t) {
                //For now, lets just ignore the error.  Later on a retry policy makes sense
                synchronized (FeedFetcher.this) {
                    isFetching = false;
                }
            }
        });
    }

    public Observable<Feed> getObservable() {
        return observable;
    }

    public Feed getLastFeed() {
        return lastFeed;
    }
}
