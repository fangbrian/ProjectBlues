package com.example.fangb.projectblues;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Date;


public class MyActivity extends Activity {

    private TextView textView;
    private Game gameInfo;
    private final Gson gson = new Gson();
    private EditText homeTeam;
    private EditText awayTeam;
    private EditText rinkName;
    private EditText keyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        //displayText(json);
        //Game gameInfo1 = gson.fromJson(json, Game.class);
        //displayText(gameInfo1.toString());
        //savePreferences("entry1", json);
        //displayText("savedpreferences: " +  loadSavedPreferences("entry1"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    private void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private String loadSavedPreferences(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String gsonName = sharedPreferences.getString(key, "Invalid Key");
        return gsonName;
    }

    private void clearSavedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void onClick_Save(View v){
        homeTeam = (EditText) findViewById(R.id.homeText);
        awayTeam = (EditText) findViewById(R.id.awayText);
        rinkName = (EditText) findViewById(R.id.rinkText);
        keyText = (EditText) findViewById(R.id.keyText);


        Date currDate = new Date(114, 1, 1, 1, 0);
        gameInfo = new Game(homeTeam.getText().toString(), awayTeam.getText().toString(), currDate, rinkName.getText().toString());
        //displayText(gameInfo.toString());
        saveGame(gameInfo, keyText.getText().toString());

        //savePreferences("entry1", json);
        displayText("savedpreferences: " +  loadSavedPreferences(keyText.getText().toString()));
    }

    private void saveGame(Game gameInstance, String key){
        String json = gson.toJson(gameInfo);
        savePreferences(key, json);
    }

    public void onClick_Clear(View v){
        clearSavedPreferences();
        displayText("Cleared Shared Preferences");
    }

    public void onClick_Display(View v){
        keyText = (EditText) findViewById(R.id.keyText);
        displayText("savedpreferences: " +  loadSavedPreferences(keyText.getText().toString()));
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
        textView = (TextView) findViewById(R.id.textDisplay);
        textView.setText(message);
    }
}
