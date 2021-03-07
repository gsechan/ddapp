package com.gabesechansoftware.ddapp.api.models;

import java.util.ArrayList;
import java.util.List;

public class RestaurantStatus {
    public String unavailable_reason;
    public boolean pickup_available;
    public boolean asap_available;
    public boolean scheduled_available;
    public List<Integer> asap_minutes_range = new ArrayList<>();
}
