package com.buinvent.buscoreapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import android.util.Base64;
//import org.apache.commons.codec.binary.Base64;

public class ChooseMatchupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_matchup);
        new RetrieveFeedTask().execute();

    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {

            try {
//                URL url = new URL("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoItem.txt");
                URL url = new URL ("https://api.mysportsfeeds.com/v1.1/pull/nfl/2017-2018-regular/daily_game_schedule.json?fordate=20171231");
                String encoding = Base64.encodeToString("buinvent:GoBucks".getBytes(), Base64.NO_WRAP);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                connection.setRequestProperty  ("Authorization", "Basic " + encoding);
                InputStream content = (InputStream)connection.getInputStream();
                BufferedReader in   =
                        new BufferedReader (new InputStreamReader (content));
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                    System.out.println(line);
                }
                return stringBuilder.toString();
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }


        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", response);
            System.out.println(response);
            // TODO: check this.exception
            // TODO: do something with the feed

//            try {
//                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
//                String requestID = object.getString("requestId");
//                int likelihood = object.getInt("likelihood");
//                JSONArray photos = object.getJSONArray("photos");
//                .
//                .
//                .
//                .
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
    }

}
