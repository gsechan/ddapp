package com.gabesechansoftware.ddapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.gabesechansoftware.ddapp.databinding.DetailActivityBinding;
import com.gabesechansoftware.ddapp.models.FeedStore;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    private final static String STORE_KEY = "store";

    private FeedStore store;
    private DetailActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        store = (FeedStore) getIntent().getSerializableExtra(STORE_KEY);
        binding = DetailActivityBinding.inflate(getLayoutInflater());
        setupHeaderImage();
        binding.name.setText(store.getName());
        binding.tags.setText(store.getTags());
        binding.rating.setText(store.getAverageRatings() + "\u2605");
        binding.numRatings.setText(getString(R.string.ratings, store.getNumRatings()));
        setContentView(binding.getRoot());
    }

    private void setupHeaderImage() {
        if(TextUtils.isEmpty(store.getHeaderImgUrl())) {
            binding.header.setVisibility(View.GONE);
        }
        else {
            Picasso.get().load(store.getHeaderImgUrl()).into(binding.header);
        }
    }

    public static Intent makeIntent(Context context, FeedStore store) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(STORE_KEY,store);
        return intent;
    }
}
