package com.example.fangb.projectblues;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.sax.Element;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.Date;


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
        testJSOUP();
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

    private void testJSOUP() {
        //URL url = new URL("http://www.pointstreak.com/players/players-division-schedule.html?divisionid=75990&seasonid=12867");

        HtmlParserTask task = new HtmlParserTask() {
            @Override
            protected void onPostExecute(String urlTitle) {
                super.onPostExecute(urlTitle);

                // onPostExecute is guaranteed to be called on the GUI thread, so it's safe
                // to call displayText here (if we tried in doInBackground, it will crash)
                displayText("Title: " + urlTitle);
            }
        };
        task.parseUrl("http://www.androidbegin.com");
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
