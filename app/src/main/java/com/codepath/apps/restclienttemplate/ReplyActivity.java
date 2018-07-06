package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ReplyActivity extends AppCompatActivity {

    EditText etReplyBody;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        etReplyBody = findViewById(R.id.etReplyBody);

        setUpText();
    }

    private void setUpText(){
        //Get the screenname of the user who posted the original tweet
        String atText = (String) Parcels.unwrap(getIntent().getParcelableExtra("userName"));
        String introText = "@" + atText + " ";
        //Set the body text of the reply to "@userName "
        etReplyBody.setText(introText);

        etReplyBody.setSelection(introText.length());

        client = TwitterApplication.getRestClient(this);
    }

    public void onClick(View view){

        client.sendTweet(etReplyBody.getText().toString(), new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("Twitterclient", response.toString());

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Twitterclient", response.toString());

                //convert each object to a tweet model
                //add that tweet model to our datasoruce
                //notify the adapter that we've added an item
                try {
                    Tweet tweet = Tweet.fromJSON(response);

                    Intent data = new Intent();
                    data.putExtra("tweet", Parcels.wrap(tweet));
                    setResult(RESULT_OK, data);
                    finish();
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Twitterclient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("Twitterclient", errorResponse.toString());
                throwable.printStackTrace();            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Twitterclient", errorResponse.toString());
                throwable.printStackTrace();             }
        });



    }


}
