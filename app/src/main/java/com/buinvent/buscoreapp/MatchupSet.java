package com.buinvent.buscoreapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MatchupSet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchup_set);

        TextView away = findViewById(R.id.away_TextView);
        TextView home = findViewById(R.id.home_TextView);

        Intent teamsIntent = getIntent();
        String awayTeam = teamsIntent.getStringExtra("away");
        String homeTeam = teamsIntent.getStringExtra("home");

        away.setText(awayTeam);
        home.setText(homeTeam);
    }
}
