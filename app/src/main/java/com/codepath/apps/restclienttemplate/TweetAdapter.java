package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.TimeFormatter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.github.scribejava.apis.TwitterApi;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    //Pass in tweets array into constructor
    private List<Tweet> mTweets;
    private Context context;
    private TwitterClient client;

    public int REQUEST_CODE = 20;

    public TweetAdapter(List<Tweet> tweets) { mTweets = tweets;
    }


    //for each row, inflate the layout and cache references into viewholder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        client = TwitterApplication.getRestClient(context);

        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);

        ViewHolder viewHolder = new ViewHolder(tweetView);

        return viewHolder;
    }

    //bind the values based on the position of the element

    @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    //get the data according to position
        final Tweet tweet = mTweets.get(position);

        final String USER_NAME = tweet.user.screenName;

        final ImageView IV_LIKE = holder.ivLike;

        // Populate the views according to this data
        holder.tvUserName.setText(tweet.user.screenName);

        holder.tvUserName.setTypeface(null, Typeface.BOLD);

        holder.tvBody.setText(tweet.body);

        holder.replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ReplyActivity.class);
                i.putExtra("userName", Parcels.wrap(USER_NAME));
                ((Activity) context).startActivityForResult(i, REQUEST_CODE);
            }
        });

        holder.likeButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if (!tweet.isLiked) {
                    client.likeTweet(tweet, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("TweetAdapter", response.toString());

                            IV_LIKE.setSelected(true);

                            tweet.isLiked = true;

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("TweetAdapter", errorResponse.toString());
                        }

                    });
                } else {
                    client.unlikeTweet(tweet, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("TweetAdapter", response.toString());

                            IV_LIKE.setSelected(false);

                            tweet.isLiked = false;

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("TweetAdapter", errorResponse.toString());
                        }
                    });
                }


            }

        });

        holder.tvName.setText(tweet.user.name);

        holder.tvName.setAlpha((float) 0.5);

        holder.tvTime.setText(tweet.time);

        Glide.with(context).load(tweet.user.profileImageUrl).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    //create viewholder class

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView ivProfileImage, ivLike;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvName;
        public TextView tvTime;
        public LinearLayoutCompat replyButton, likeButton;

        public ViewHolder (View itemView){
            super(itemView);

            //perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            replyButton = (LinearLayoutCompat) itemView.findViewById(R.id.replyButton);
            likeButton = (LinearLayoutCompat) itemView.findViewById(R.id.likeButton);
            ivLike = (ImageView) itemView.findViewById(R.id.ivLike);
        }


    }

    //Methods involved with swipe refresher

    //Clean all elements of the recycler
    public void clear(){
        mTweets.clear();
        notifyDataSetChanged();
    }

    //Add a list of items == change to type used
    public void addAll(List<Tweet> list){
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

}
