package com.buinvent.buscoreapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.utils.Async;

public class MatchupSetActivity extends AppCompatActivity {

    // Set macros for string extras
    public static final String EXTRA_AWAY_TEAM = "com.buinvent.buscoreapp.AWAY_TEAM";
    public static final String EXTRA_HOME_TEAM = "com.buinvent.buscoreapp.HOME_TEAM";
    public static final String EXTRA_LEAGUE = "com.buinvent.buscoreapp.LEAGUE";
    public static final String EXTRA_ABBREVIATION = "com.buinvent.buscoreapp.ABBREVIATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchup_set);

        // Grab the textviews from activity_matchup_set.xml
        TextView away = findViewById(R.id.away_TextView);
        TextView home = findViewById(R.id.home_TextView);
        TextView matchupSet = findViewById(R.id.matchup_set_text);
        TextView vsText = findViewById(R.id.vs_TextView);

        // Grab the extra strings that were set in ChooseMatchupActivity
        Intent teamsIntent = getIntent();
        String awayTeam = teamsIntent.getStringExtra(EXTRA_AWAY_TEAM);
        String homeTeam = teamsIntent.getStringExtra(EXTRA_HOME_TEAM);
        String mLeague = teamsIntent.getStringExtra(EXTRA_LEAGUE);
        String teamAbbreviation = teamsIntent.getStringExtra(EXTRA_ABBREVIATION);


        // Login to Particle Cloud and call the Photon's "team_league" function and pass in the
        // user's selected home team abbreviation and league
        ParticleCloudSDK.init(this);

        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {

            private ParticleDevice mDevice;

            @Override
            public Object callApi(@NonNull ParticleCloud sparkCloud) throws ParticleCloudException, IOException {

                sparkCloud.logIn("jmmorris2@mail.bradley.edu", "GoBucks");
                mDevice = sparkCloud.getDevice("350036001247343438323536");

                List<String> teamLeague = new ArrayList<>();
                teamLeague.add( mLeague.toUpperCase() + " " + teamAbbreviation );

                try {
                    mDevice.callFunction("team_league", teamLeague);
                } catch (Exception e) {
                    throw new ParticleCloudException(e);
                }

                return -1;
            }

            // Tell the user the matchup has been set if they were successfully logged in to the
            // Particle cloud and the team_league function was called successfully
            @Override
            public void onSuccess(@NonNull Object value) {

                matchupSet.setText(R.string.header_matchup_set);
                vsText.setText(R.string.vs_text);
                away.setText(awayTeam);
                home.setText(homeTeam);
            }

            // Tell the user the matchup has not been set if they were successfully logged in to the
            // Particle cloud and the team_league function was called successfully
            @Override
            public void onFailure(@NonNull ParticleCloudException e) {
                matchupSet.setText("Error!!!");
                vsText.setText("Matchup could not be set");
            }
        });
    }
}
