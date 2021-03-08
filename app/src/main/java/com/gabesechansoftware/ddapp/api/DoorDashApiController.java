package com.gabesechansoftware.ddapp.api;

import com.gabesechansoftware.ddapp.api.models.RestaurantsNearLocationResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Wrapper around the Door Dash API that initializes RetroFit and parsers as needed.
 * Meant to be a singleton
 */
public class DoorDashApiController {
    static final String BASE_URL = "https://api.doordash.com";
    final DoorDashApi doorDashApi;

    public DoorDashApiController() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        doorDashApi = retrofit.create(DoorDashApi.class);

    }

    public void getFeed(double lat,
                        double lng,
                        int offset,
                        int limit,
                        Callback<RestaurantsNearLocationResult> callback){
        doorDashApi.feedByLatLng(lat, lng, offset, limit).enqueue(callback);
    }
}
