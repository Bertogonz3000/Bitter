package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.TimeFormatter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.w3c.dom.Text;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    //Pass in tweets array into constructor
    private List<Tweet> mTweets;
    Context context;

    public TweetAdapter(List<Tweet> tweets) { mTweets = tweets;
    }


    //for each row, inflate the layout and cache references into viewholder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);

        ViewHolder viewHolder = new ViewHolder(tweetView);

        return viewHolder;
    }

    //bind the values based on the position of the element

    @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    //get the data according to position
        Tweet tweet = mTweets.get(position);

        // Populate the views according to this data
        holder.tvUserName.setText(tweet.user.screenName);

        holder.tvUserName.setTypeface(null, Typeface.BOLD);

        holder.tvBody.setText(tweet.body);

        holder.tvName.setText(tweet.user.name);

        holder.tvName.setAlpha((float) 0.5);

        holder.tvTime.setText(tweet.time);

        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    //create viewholder class

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvName;
        public TextView tvTime;

        public ViewHolder (View itemView){
            super(itemView);

            //perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        }
    }
}
