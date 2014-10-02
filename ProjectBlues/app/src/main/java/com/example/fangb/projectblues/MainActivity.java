package com.example.fangb.projectblues;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
 //TODO: check for internet connectivity
 //TODO: Notification on the day of the game

public class MainActivity extends Activity {

    private final Gson gson = new Gson();
    private EditText homeTeam;
    private EditText awayTeam;
    private EditText rinkName;
    private EditText keyText;
    private TextView textView;
    private TextView displayStandings;
    private String url;
    private String title;
    private List<Game> gamesScheduled = null;

    private SharedPreferences prefs;
    private final static String SHARED_PREFS_NAME = "BluesPreferences";
    private final static String MY_TEAM = "Knights";
    private final static String NEXT_GAME = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textDisplay);
        displayStandings = (TextView) findViewById(R.id.textStandings);
        prefs = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        //displayText(json);
        //Game gameInfo1 = gson.fromJson(json, Game.class);
        //displayText(gameInfo1.toString());
        //savePreferences("entry1", json);
        //displayText("savedpreferences: " +  loadSavedPreferences("entry1"));

        //url = "http://www.pointstreak.com/players/players-division-schedule.html?divisionid=75990&seasonid=12867";
        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {

                if(isNetworkAvailable()) {
                    //Clear Saved Preferences
                    clearSavedPreferences();
                    //Gather Games
                    gatherGames();
                    //Gather Standings
                    gatherStandings();
                } else {
                    displayText("No Internet Connection");
                }
            }
        };

        // schedule the task to run starting now and then every hour...
        timer.schedule (hourlyTask, 0l, 1000*60*60);

    }

    // TODO: Use SherlockActionBar for backwards compatability
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void savePreferences(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String loadSavedPreferences(String key) {
        return prefs.getString(key, "Invalid Key");
    }

    private void clearSavedPreferences(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    public void save(){

        for(int i = 0; i < gamesScheduled.size(); i++) {
            Game currGame = gamesScheduled.get(i);
            String key;
            key = Integer.toString(i);

            //Key for shared preferences is now the opposing team
            saveJSON(currGame, key);
        }
    }

    private void saveJSON(Game game, String key){
        String json = gson.toJson(game);
        savePreferences(key, json);
    }

    private Game convertJSONToGame(String json){
        Game game = gson.fromJson(json, Game.class);
        return game;
    }

    private void updateNextGame(){

        if(prefs.contains(NEXT_GAME)){
            String jsonGame = loadSavedPreferences(NEXT_GAME);
            Game game = convertJSONToGame(jsonGame);
            displayText(game.displayGame());

        } else{
            displayText("Error");
        }
    }

    private void gatherGames() {

        PointstreakParser task = new PointstreakParser() {
            @Override
            protected void onPostExecute(List<Game> games) {
                super.onPostExecute(games);

                // onPostExecute is guaranteed to be called on the GUI thread, so it's safe
                // to call displayText here (if we tried in doInBackground, it will crash)

                if(games != null){
                    gamesScheduled = games;
                }
                save();
                updateNextGame();
                //TODO:parse through games to see if there is a game today
                //if internet not available use shared preferences
                //if available used most up to date schedule
                //TODO:if there is a game today, send notification
            }
        };
        task.execute();
    }

    private void gatherStandings() {

        StandingsParser task = new StandingsParser() {
            @Override
            protected void onPostExecute(String teams) {
                super.onPostExecute(teams);

                // onPostExecute is guaranteed to be called on the GUI thread, so it's safe
                // to call displayText here (if we tried in doInBackground, it will crash)

                if(teams != null){
                    printStandings(teams);
                }
            }
        };
        task.execute();
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

    private void displayText(String message) {
        textView.setText(message);
    }

    private void printStandings(String message){
        displayStandings.setText(message);
    }
}
