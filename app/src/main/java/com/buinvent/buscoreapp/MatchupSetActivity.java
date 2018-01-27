package com.buinvent.buscoreapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.Toaster;

public class MatchupSetActivity extends AppCompatActivity {
    public static final String EXTRA_AWAY_TEAM = "com.buinvent.buscoreapp.AWAY_TEAM";
    public static final String EXTRA_HOME_TEAM = "com.buinvent.buscoreapp.HOME_TEAM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchup_set);

        TextView away = findViewById(R.id.away_TextView);
        TextView home = findViewById(R.id.home_TextView);

        Intent teamsIntent = getIntent();
        String awayTeam = teamsIntent.getStringExtra(EXTRA_AWAY_TEAM);
        String homeTeam = teamsIntent.getStringExtra(EXTRA_HOME_TEAM);

        away.setText(awayTeam);
        home.setText(homeTeam);


        ParticleCloudSDK.init(this);

        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {

            @Override
            public Object callApi(@NonNull ParticleCloud sparkCloud) throws ParticleCloudException, IOException {
                sparkCloud.logIn("jmmorris2@mail.bradley.edu", "GoBucks");
                return -1;

            }

            @Override
            public void onSuccess(@NonNull Object value) {
                Toaster.l(MatchupSetActivity.this, "Logged in");
            }

            @Override
            public void onFailure(@NonNull ParticleCloudException e) {
                Toaster.l(MatchupSetActivity.this, e.getBestMessage());
                e.printStackTrace();
                Log.d("info", e.getBestMessage());
            }
        });


    }


//    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
//
//        protected void onPreExecute() {}
//
//        protected String doInBackground(Void... values) {
//
//                try {
//                    ParticleCloudSDK.getCloud().logIn("jmmorris2@mail.bradley.edu", "GoBucks");
//                    Toaster.l(MatchupSetActivity.this, "Logged in");
//
//                } catch (final ParticleCloudException e) {
//                    Runnable mainThread = () -> {
//                        Toaster.l(MatchupSetActivity.this, e.getBestMessage());
//                        e.printStackTrace();
//                        Log.d("info", e.getBestMessage());
//                    };
//                    runOnUiThread(mainThread);
//
//                }
//
//                return null;
//            }
//        }
    }
