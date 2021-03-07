package com.gabesechansoftware.ddapp.api.models;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsNearLocationResult {
    public int num_results;
    public int next_offset;
    public List<RestaurantNearLocationStore> stores = new ArrayList<>();
}
