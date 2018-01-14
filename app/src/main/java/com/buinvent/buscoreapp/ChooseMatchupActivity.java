package com.buinvent.buscoreapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChooseMatchupActivity extends AppCompatActivity {

    /* Data API URL and authentication. */
    private static final String API_USERNAME = "buinvent";
    private static final String API_PASSWORD = "GoBucks";

    /* Debug tag. */
    private static final String TAG = "ChooseMatchupActivity";

    /* Matchup XML content */
    LinearLayout ll;
    LayoutParams lp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_matchup);

        /* get the league that was selected and current date */
        Intent leagueIntent = getIntent();
        String league = leagueIntent.getStringExtra("league");
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());

        /* initialize API URL */
        String apiFeedUrl = "https://api.mysportsfeeds.com/v1.1/pull/" + league +
                "/current/daily_game_schedule.json?fordate=" + timeStamp;

        // Initialize XML content
        ll = findViewById(R.id.matchup_layout);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);


        // Set API Authentication.
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(API_USERNAME, API_PASSWORD.toCharArray());
            }
        });

        // Get data feed from API
        new RetrieveFeedTask().execute(apiFeedUrl);
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

                // grab the current dates matchups in a JSONArray
                JSONObject obj = new JSONObject(response);
                JSONArray games = obj.getJSONObject("dailygameschedule").getJSONArray("gameentry");

                /* grab all of the home and away team's names for every matchup, add them to a button,
                   and add that button to the layout                       */
                for (int i = 0; i < games.length(); i++){
                    
                    String awayTeam = games.getJSONObject(i).getJSONObject("awayTeam").getString("Name").toLowerCase();
                    String homeTeam = games.getJSONObject(i).getJSONObject("homeTeam").getString("Name").toLowerCase();
                    String matchUpStr = awayTeam + "\n" + homeTeam;

                    int awayID = getApplicationContext().getResources().getIdentifier("nfl_" + awayTeam, "drawable", getApplicationContext().getPackageName());
                    int homeID = getApplicationContext().getResources().getIdentifier("nfl_" + homeTeam, "drawable", getApplicationContext().getPackageName());

                    BitmapDrawable awayDrawable = (BitmapDrawable) getDrawable(awayID);
                    BitmapDrawable homeDrawable = (BitmapDrawable) getDrawable(homeID);

                    Drawable[] drawables = new Drawable[2];
                    drawables[0] = awayDrawable;
                    drawables[1] = homeDrawable;

                    LayerDrawable layerDrawable = new LayerDrawable(drawables);
                    // Notice here how the top of this layer is the button text size. This is opposite of the
                    // other drawable whom's bottom will be the button text size.
                    layerDrawable.setLayerInset(0,0, -75,0,0);
                    layerDrawable.setLayerInset(1,0, 0,0,-75);

                    Button button = new Button(getApplicationContext());
                    button.setTextSize(30);
                    button.setGravity(Gravity.START);
                    button.setText(matchUpStr);
                    button.setCompoundDrawablesWithIntrinsicBounds(layerDrawable, null,null,null);

                    ll.addView( button, lp);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private BitmapDrawable resizeDrawable(int sizeOfDrawable, BitmapDrawable bitmapDrawable) {
            Bitmap bitmap = bitmapDrawable.getBitmap();
            BitmapDrawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap,
                    sizeOfDrawable,
                    sizeOfDrawable, true));
            return d;
        }

    }
}
