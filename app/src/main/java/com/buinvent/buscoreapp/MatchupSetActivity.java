package com.buinvent.buscoreapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.Toaster;

public class MatchupSetActivity extends AppCompatActivity {
    public static final String EXTRA_AWAY_TEAM = "com.buinvent.buscoreapp.AWAY_TEAM";
    public static final String EXTRA_HOME_TEAM = "com.buinvent.buscoreapp.HOME_TEAM";
    public static final String EXTRA_LEAGUE = "com.buinvent.buscoreapp.LEAGUE";
    public static final String EXTRA_ABBREVIATION = "com.buinvent.buscoreapp.ABBREVIATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchup_set);

        TextView away = findViewById(R.id.away_TextView);
        TextView home = findViewById(R.id.home_TextView);

        Intent teamsIntent = getIntent();

        String awayTeam = teamsIntent.getStringExtra(EXTRA_AWAY_TEAM);
        String homeTeam = teamsIntent.getStringExtra(EXTRA_HOME_TEAM);
        String mLeague = teamsIntent.getStringExtra(EXTRA_LEAGUE);
        String teamAbbreviation = teamsIntent.getStringExtra(EXTRA_ABBREVIATION);

        away.setText(awayTeam);
        home.setText(homeTeam);

        ParticleCloudSDK.init(this);

        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {

            private ParticleDevice mDevice;

            @Override
            public Object callApi(@NonNull ParticleCloud sparkCloud) throws ParticleCloudException, IOException {

                sparkCloud.logIn("jmmorris2@mail.bradley.edu", "GoBucks");
                mDevice = sparkCloud.getDevice("350036001247343438323536");

                try {
                    List<String> teamLeague = new ArrayList<>();
                    teamLeague.add( mLeague.toUpperCase() + " " + teamAbbreviation );

                    mDevice.callFunction("team_league", teamLeague);

                } catch (Exception e) {
                    e.printStackTrace();
                }

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
}
