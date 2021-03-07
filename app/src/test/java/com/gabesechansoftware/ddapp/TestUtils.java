package com.gabesechansoftware.ddapp;

import com.gabesechansoftware.ddapp.api.models.RestaurantNearLocationStore;
import com.gabesechansoftware.ddapp.api.models.RestaurantsNearLocationResult;

public class TestUtils {
    static public RestaurantsNearLocationResult createResultWithStores(int numResults, int numStores)  {
        RestaurantsNearLocationResult result = new RestaurantsNearLocationResult();
        result.num_results = numResults;
        for(int i=0; i < numStores; i++) {
            result.stores.add(new RestaurantNearLocationStore());
        }
        return result;
    }

}
