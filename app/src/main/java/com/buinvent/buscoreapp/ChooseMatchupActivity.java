package com.buinvent.buscoreapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;

public class ChooseMatchupActivity extends AppCompatActivity {

    /* Data API URL and authentication. */
    private static final String API_FEED_URL = "https://api.mysportsfeeds.com/v1.1/pull/nfl/2017-2018-regular/daily_game_schedule.json?fordate=20171231";
    private static final String API_USERNAME = "buinvent";
    private static final String API_PASSWORD = "GoBucks";

    /* Debug tag. */
    private static final String TAG = "ChooseMatchupActivity";

    /* Matchup XML content */
    LinearLayout ll;
    LayoutParams lp;
    Button matchupButtons[] = new Button[50];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_matchup);

        // Initialize XML content
        ll = findViewById(R.id.matchup_layout);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        for(int i = 0; i < 50; i++) {
            matchupButtons[i] = new Button(this);
            matchupButtons[i].setTextSize(30);
            matchupButtons[i].setGravity(Gravity.START);
        }


        // Set API Authentication.
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(API_USERNAME, API_PASSWORD.toCharArray());
            }
        });

        // Get data feed from API
        new RetrieveFeedTask().execute(API_FEED_URL);
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {}

        protected String doInBackground(String... urls) {
            String url = urls[0];
            String response = null;

            try {
                // Set up HTTP Connection
                URLConnection connection = new URL(url).openConnection();
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader in = new BufferedReader(streamReader);

                // Read HTTP response.
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                response = stringBuilder.toString();

            } catch(Exception e) {
                Log.e(TAG, "Exception", e);
            }

            return response;
        }

        protected void onPostExecute(String response) {
            Log.v(TAG, "HTTP Response: " + response);

            /* Grab all matchups from API and add them to the layout */
            try{

                JSONObject obj = new JSONObject(response);
                JSONArray games = obj.getJSONObject("dailygameschedule").getJSONArray("gameentry");

                for (int i = 0; i < games.length(); i++){
                    
                    String awayTeam = games.getJSONObject(i).getJSONObject("awayTeam").getString("Name");
                    String homeTeam = games.getJSONObject(i).getJSONObject("homeTeam").getString("Name");
                    String matchUpStr = awayTeam + "\n" + homeTeam;

                    matchupButtons[i].setText(matchUpStr);
                    ll.addView(matchupButtons[i], lp);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
