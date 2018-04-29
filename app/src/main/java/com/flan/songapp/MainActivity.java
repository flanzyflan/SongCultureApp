package com.flan.songapp;


import android.app.DownloadManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    // This is a test comment 4/23/18 11:30 am
    private static RequestQueue requestQueue;

    private static final String TAG = "SongApp:Main";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_main);
        startAPICall();

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
            }
        });
    }
    void startAPICall() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://api.themoviedb.org/3/search/movie?api_key="
                            + "bbec13ad5f0b916db99eef893310f76c" + "&language=en-US&query=" + "" + "&page=1&include_adult=false",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                Log.d(TAG, response.toString(2));
                            } catch (JSONException ignored) { }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getDetails(final String json) {
        if (json == null) {
            return "Nothing To Show";
        }
        JsonParser parser = new JsonParser();
        JsonObject result = parser.parse(json).getAsJsonObject();
        if (result == null) {
            return "Nothing To Show";
        }
        int totalResults = result.get("total_results").getAsInt();
        if (totalResults == 0) {
            return "No such movie exists. Try fixing any typos or searching for a different movie.";
        }
        JsonArray results = result.get("results").getAsJsonArray();
        JsonObject details = results.get(0).getAsJsonObject();
        String voteAverage = details.get("vote_average").getAsString();
        String title = details.get("title").getAsString();
        String overview = details.get("overview").getAsString();
        String releaseDate = details.get("release_date").getAsString();
        return "Title: " + title + "/n" + "Description: " + overview + "/n" + "Rating: " + voteAverage+ "/n" + "Release Date: " + releaseDate;
    }
}


