package com.flan.songapp;


import android.app.DownloadManager;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

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
    private String json;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_main);

        
        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                final EditText edit = findViewById(R.id.TextBox1);
                String search = edit.getText().toString();
                search = search.replace(" ", "%20");
                startAPICall(search);
                TextView answer = findViewById(R.id.JsonResult);
                //Find where json response is and put it in argument for getDetails. Thx
                answer.setText(getDetails(json));
            }
        });
    }
    void startAPICall(String search) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://api.themoviedb.org/3/search/movie?api_key="
                            + "bbec13ad5f0b916db99eef893310f76c" + "&language=en-US&query=" + search + "&page=1&include_adult=false",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                Log.d(TAG, response.toString(2));
                                json = response.toString();
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
    public String getDetails(final String json) {
        if (json == null) {
            return "";
        }
        JsonParser parser = new JsonParser();
        JsonObject result = parser.parse(json).getAsJsonObject();
        if (result == null) {
            return "";
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
        String Genre = "";
        JsonArray genres= details.get("genre_ids").getAsJsonArray();
        for (int i = 0; i < genres.size(); i++) {
            Genre = Genre + getGenre(genres.get(i).getAsInt()) + "\n";
        }
        return "Title: " + title + "\n\n" + "Description: " + overview + "\n\n" + "Genres: \n" + Genre + "\n" + "Rating: " + voteAverage + "/10" + "\n\n" + "Release Date: " + releaseDate;
    }
    public String getGenre(final int x) {
        int[] number = new int[]{28, 12, 16, 35, 80, 99, 18, 10751, 14, 36, 27, 10402, 9648, 10749, 878, 10770, 53, 10752, 37};
        String[] genre = new String[]{"Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary", "Drama", "Family", "Fantasy", "History", "Horror", "Music", "Mystery", "Romance", "Science Fiction", "TV Movie", "Thriller", "War", "Western"};
        for (int i = 0; i < number.length; i++) {
            if (number[i] == x) {
                return genre[i];
            }
        }
        return "";
    }
}


