package com.example.fangb.projectblues;

import android.app.Activity;
import android.content.SharedPreferences;
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


public class MainActivity extends Activity implements View.OnClickListener {

    private final Gson gson = new Gson();
    private EditText homeTeam;
    private EditText awayTeam;
    private EditText rinkName;
    private EditText keyText;
    private TextView textView;
    private String url;
    private String title;

    private SharedPreferences prefs;
    private final static String SHARED_PREFS_NAME = "BluesPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeTeam = (EditText) findViewById(R.id.homeText);
        awayTeam = (EditText) findViewById(R.id.awayText);
        rinkName = (EditText) findViewById(R.id.rinkText);
        keyText = (EditText) findViewById(R.id.keyText);
        textView = (TextView) findViewById(R.id.textDisplay);

        findViewById(R.id.saveButton).setOnClickListener(this);
        findViewById(R.id.clearButton).setOnClickListener(this);
        findViewById(R.id.displayButton).setOnClickListener(this);

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
                //Gather Games
                gatherGames();
                //TODO:Save to Shared Preferences
                //TODO:Clear Memory Allocation for games
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
        Date currDate = new Date(114, 1, 1, 1, 0);
        Game gameInfo = new Game(homeTeam.getText().toString(), awayTeam.getText().toString(),
                currDate, rinkName.getText().toString());
        saveGame(gameInfo, keyText.getText().toString());

        displayText("savedpreferences: " +  loadSavedPreferences(keyText.getText().toString()));
    }

    private void saveGame(Game game, String key){
        String json = gson.toJson(game);
        savePreferences(key, json);
    }

    private void gatherGames() {
        //URL url = new URL("http://www.pointstreak.com/players/players-division-schedule.html?divisionid=75990&seasonid=12867");

        PointstreakParser task = new PointstreakParser() {
            @Override
            protected void onPostExecute(List<Game> games) {
                super.onPostExecute(games);

                // onPostExecute is guaranteed to be called on the GUI thread, so it's safe
                // to call displayText here (if we tried in doInBackground, it will crash)
                //displayText(test);

                if(games != null){
                    Game test = games.get(1);
                    displayText(test.toString());
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveButton:
                save();
                break;

            case R.id.clearButton:
                clearSavedPreferences();
                displayText("Cleared Shared Preferences");
                break;

            case R.id.displayButton:
                displayText("savedpreferences: " +  loadSavedPreferences(keyText.getText().toString()));
                break;
        }
    }
}
