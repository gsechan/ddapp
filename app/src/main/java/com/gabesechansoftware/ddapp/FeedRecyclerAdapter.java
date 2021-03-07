package com.gabesechansoftware.ddapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gabesechansoftware.ddapp.databinding.FeedStoreBinding;
import com.gabesechansoftware.ddapp.feedfetcher.FeedFetcher;
import com.gabesechansoftware.ddapp.models.Feed;
import com.gabesechansoftware.ddapp.models.FeedStore;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Recycler adapter for the feed.  Adds in a special loading item for when we have more data coming
 * and handles requesting of additional data when we come near the end of the list
 */
public class FeedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_STORE = 0;
    private static final int TYPE_LOADING = 1;

    private final static int PREFETCH_DISTANCE = 20;

    private Feed currentFeed;
    private final FeedFetcher feedFetcher;

    FeedRecyclerAdapter(FeedFetcher feedFetcher) {
        this.feedFetcher = feedFetcher;
        currentFeed = feedFetcher.getLastFeed();
        feedFetcher.getObservable().subscribe(feed-> {
           currentFeed = feed;
           notifyDataSetChanged();
        });
    }

    static class ViewHolderLoading extends RecyclerView.ViewHolder {
        ViewHolderLoading(View v) {
            super(v);
        }

        public void bind(){}
    }

    static class ViewHolderStore extends RecyclerView.ViewHolder {
        FeedStoreBinding binding;

        ViewHolderStore(FeedStoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(FeedStore store) {
            binding.name.setText(store.getName());
            binding.tags.setText(store.getTags());
            if(store.isOpen()) {
                binding.time.setText(itemView.getContext().getString(R.string.minutesAway, store.getMaxMinutes()));
            }
            else {
                binding.time.setText(R.string.closed);
            }
            Picasso.get().load(store.getCoverImgUrl()).into(binding.cover);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position >= currentFeed.getNumStores() ? TYPE_LOADING : TYPE_STORE;
    }

    @Override
    public @NonNull
    RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                  int viewType) {
        switch(viewType) {
            case TYPE_LOADING:
                View loading =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_loading,
                                parent,
                                false);
                return new ViewHolderLoading(loading);
            case TYPE_STORE:
                FeedStoreBinding binding =
                        FeedStoreBinding.inflate(LayoutInflater.from(parent.getContext()),
                                parent,
                                false);
                return new ViewHolderStore(binding);
            default:
                throw new RuntimeException("Got an invalid view type in feed");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position < currentFeed.getNumStores()) {
            FeedStore store = currentFeed.getStores().get(position);
            ((ViewHolderStore)holder).bind(store);
        }
        else {
            ((ViewHolderLoading)holder).bind();
        }
        if(!currentFeed.isFullyFetched() &&
                position > currentFeed.getNumStores() - PREFETCH_DISTANCE) {
            //We're close to the end, lets get the next batch of stores
            feedFetcher.precache();
        }
    }

    @Override
    public int getItemCount() {
        //If the feed is not fully loaded, stick a loading item in as well as the loaded stores
        int stores = currentFeed.getNumStores();
        return currentFeed.isFullyFetched() ? stores : stores + 1;
    }

}
