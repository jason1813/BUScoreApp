package com.buinvent.buscoreapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChooseLeagueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_league);
    }

    public void onLeagueSelected(View v) {
        switch(v.getId()) {
            case R.id.button_nfl:
                startMatchupActivity("nfl");
                break;
            case R.id.button_nba:
                startMatchupActivity("nba");
                break;
            case R.id.button_nhl:
                startMatchupActivity("nhl");
                break;
            case R.id.button_mlb:
                startMatchupActivity("mlb");
                break;
        }
    }

    private void startMatchupActivity(String league) {

        Intent chooseLeague = new Intent(ChooseLeagueActivity.this, ChooseMatchupActivity.class);
        chooseLeague.putExtra("league", league);
        startActivity(chooseLeague);

    }
}
