package com.cornez.mazechase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WinActivity extends Activity {
    public final String MY_PREFERENCES = "My Prefs";
    private RelativeLayout relativeLayout;
    private TextView winCounter;
    private Button restartButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        relativeLayout = (RelativeLayout)findViewById(R.id.activity_win);
        restartButton = (Button)findViewById(R.id.restart_btn);
        winCounter = (TextView)findViewById(R.id.win_counter);

        SharedPreferences pref = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        int numWins = pref.getInt("wins", -1);
        winCounter.setText("You've won " + numWins + " times!");
        //on click listener for restart button
        restartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent restartIntent = new Intent(WinActivity.this, MyActivity.class);
                startActivity(restartIntent);
            }
        });
    }

}
