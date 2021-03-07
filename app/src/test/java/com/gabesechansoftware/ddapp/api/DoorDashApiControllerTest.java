package com.gabesechansoftware.ddapp.api;

import com.gabesechansoftware.ddapp.api.models.RestaurantsNearLocationResult;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoorDashApiControllerTest {

    //Note:  this test may be flaky, relies on an external server
    @Test
    public void testCanFetchFeed() throws InterruptedException {
        final CountDownLatch lock = new CountDownLatch(1);
        DoorDashApiController controller = new DoorDashApiController();
        controller.getFeed(37.422740,
                -122.139956,
                0,
                50,
                new Callback<RestaurantsNearLocationResult>() {
                    @Override
                    public void onResponse(Call<RestaurantsNearLocationResult> call, Response<RestaurantsNearLocationResult> response) {
                        assertTrue(response.isSuccessful());
                        assertNotEquals(0, response.body().stores.size());
                        lock.countDown();
                    }

                    @Override
                    public void onFailure(Call<RestaurantsNearLocationResult> call, Throwable t) {
                        fail();
                    }
                });
        lock.await(2000, TimeUnit.MILLISECONDS);
        assertEquals(0, lock.getCount());
    }
}
