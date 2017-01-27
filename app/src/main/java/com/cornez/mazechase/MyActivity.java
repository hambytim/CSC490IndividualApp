package com.cornez.mazechase;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.util.Random;


public class MyActivity extends Activity {

    public final String MY_PREFERENCES = "My Prefs";
    private RelativeLayout relativeLayout;
    private int xPos;
    private int yPos;
    private MazeCanvas maze;
    private int numWins;
    private int counter = 0;


    private int cellId;
    private int starCell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //create/load number of wins to display on win screen
        SharedPreferences pref = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        numWins = pref.getInt("wins", -1);
        if(numWins == -1){
            editor.putInt("wins", 0);
            numWins = 0;
            editor.commit();
        }
        setContentView(R.layout.activity_my);
        xPos = 0;
        yPos = 0;
        cellId = 0;

        final Random rand = new Random();
        // CONSTRUCT THE MAZE AND ADD IT TO THE RELATIVE LAYOUT
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        maze = new MazeCanvas(this);
        relativeLayout.addView(maze,0);
        starCell = maze.getStarCell();

        //thread to move star around map

        Thread starThread = new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(3000 + 500*counter);
                        int randCell = rand.nextInt(maze.board.length);
                        starCell = randCell;
                        maze.setStarCell(randCell);
                        maze.postInvalidate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    counter++;
                }
            }
        });
        starThread.start();
    }

    public void goUp(View view) {
        if (maze.board[cellId].north == false){
            cellId = maze.getCellID();
            cellId -= maze.COLS;
            yPos = maze.board[cellId].y + 10;
            maze.setRobotY(yPos);
            maze.setCellID(cellId);
            maze.postInvalidate();
        }
        if(cellId == starCell){
            increaseWins();
            launchWinScreen();
        }
    }

    public void goLeft(View view) {
        if (maze.board[cellId].west == false){
            cellId = maze.getCellID();
            cellId--;
            xPos = maze.board[cellId].x + 10;
            maze.setRobotX(xPos);
            maze.setCellID(cellId);
            maze.postInvalidate();
        }
        if(cellId == starCell){
            increaseWins();
            launchWinScreen();
        }
    }

    public void goRight(View view) {
        if (maze.board[cellId].east == false){
            cellId = maze.getCellID();
            cellId++;
            xPos = maze.board[cellId].x + 10;
            maze.setRobotX(xPos);
            maze.setCellID(cellId);
            maze.postInvalidate();
        }
        if(cellId == starCell){
            increaseWins();
            launchWinScreen();
        }
    }

    public void goDown(View view) {
        if (maze.board[cellId].south == false){
            cellId = maze.getCellID();
            cellId += maze.COLS;
            yPos = maze.board[cellId].y + 10;
            maze.setRobotY(yPos);
            maze.setCellID(cellId);
            maze.postInvalidate();
        }
        if(cellId == starCell){
            increaseWins();
            launchWinScreen();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void launchWinScreen(){
        Intent winIntent = new Intent(MyActivity.this, WinActivity.class);
        startActivity(winIntent);
    }

    public void increaseWins(){
        numWins++;
        SharedPreferences pref = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("wins", numWins);
        editor.commit();
    }

}
