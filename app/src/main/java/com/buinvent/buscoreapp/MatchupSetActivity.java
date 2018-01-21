package com.buinvent.buscoreapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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
    }
}
