package com.gabesechansoftware.ddapp.api;
import com.gabesechansoftware.ddapp.api.models.RestaurantsNearLocationResult;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit API interface for making calls to Door Dash
 */
interface DoorDashApi {

    @GET("/v1/store_feed/")
    Call<RestaurantsNearLocationResult> feedByLatLng(@Query("lat") double lat,
                                                           @Query("lng") double lng,
                                                           @Query("offset") int offset,
                                                           @Query("limit") int limit);

}
