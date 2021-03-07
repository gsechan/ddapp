package com.gabesechansoftware.ddapp.api.models;
import com.gabesechansoftware.ddapp.models.FeedStore;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class RestaurantNearLocationStoreTest {

    @Test
    public void testConversionIsOpenCorrect() {
        RestaurantNearLocationStore pojo = new RestaurantNearLocationStore();
        pojo.cover_img_url = "";
        pojo.description ="";
        pojo.name = "";
        pojo.id = 1;
        pojo.url = "";
        pojo.status= new RestaurantStatus();
        pojo.status.unavailable_reason="Closed";
        FeedStore store = pojo.toFeedStore();
        assertFalse(store.isOpen());

        pojo.status.unavailable_reason=null;
        store = pojo.toFeedStore();
        assertTrue(store.isOpen());

    }

    @Test
    public void testEmptyMinutesDoesNotCrash() {
        RestaurantNearLocationStore pojo = new RestaurantNearLocationStore();
        pojo.cover_img_url = "";
        pojo.description ="";
        pojo.name = "";
        pojo.id = 1;
        pojo.url = "";
        pojo.status= new RestaurantStatus();
        pojo.status.unavailable_reason="Closed";
        FeedStore store = pojo.toFeedStore();
        assertEquals(100, store.getMaxMinutes());
    }

    @Test
    public void testMinutesGetsMax() {
        RestaurantNearLocationStore pojo = new RestaurantNearLocationStore();
        pojo.cover_img_url = "";
        pojo.description = "";
        pojo.name = "";
        pojo.id = 1;
        pojo.url = "";
        pojo.status = new RestaurantStatus();
        pojo.status.unavailable_reason = "Closed";
        pojo.status.asap_minutes_range = Arrays.asList(10, 20);
        FeedStore store = pojo.toFeedStore();
        assertEquals(20, store.getMaxMinutes());
        pojo.status.asap_minutes_range = Arrays.asList(20, 10);
        store = pojo.toFeedStore();
        assertEquals(20, store.getMaxMinutes());
    }
}
