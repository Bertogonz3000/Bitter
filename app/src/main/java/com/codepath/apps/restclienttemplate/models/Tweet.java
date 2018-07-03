package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {

    //List out the attributes
    public String body;
    public long uid; //database ID for the tweet
    public User user;
    public String createdAt;
    public String time;

    // de-serialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{

        Tweet tweet = new Tweet();

        //extract all the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.time = TimeFormatter.getTimeDifference(tweet.createdAt);

        return tweet;
    }
}
