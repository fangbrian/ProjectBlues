package com.example.fangb.projectblues;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;




/**
 * Created by fangb on 12/22/2014.
 */
public class ScheduleFragment extends Fragment {

    private ListView gameList;
    private SharedPreferences prefs;
    private final static String SHARED_PREFS_NAME = "BluesPreferences";
    private List<Game> gamesScheduled = null;
    private final static String NEXT_GAME = "0";
    private final Gson gson = new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.schedule_fragment, null);
        gameList = (ListView) rootView.findViewById(R.id.gamelist);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        parseHtmlGames();


    }

    public void parseHtmlGames(){
        prefs = this.getActivity().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        //initializeNotification();

        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {

                if(isNetworkAvailable()) {
                    //Clear Saved Preferences
                    clearSavedPreferences();
                    //Gather Games
                    gatherGames();


                } else {
                    if(prefs.contains(NEXT_GAME)){

                    } else{

                    }

                }
            }
        };

        //Schedule updates every hour
        timer.schedule (hourlyTask, 0l, 1000*60*60);

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void clearSavedPreferences(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
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
                updateListOfGames();

                //TODO:parse through games to see if there is a game today
                //if internet not available use shared preferences
                //if available used most up to date schedule
                //TODO:if there is a game today, send notification
            }
        };
        task.execute();
    }

    public void save(){

        for(int i = 0; i < gamesScheduled.size(); i++) {
            Game currGame = gamesScheduled.get(i);
            String key;
            key = Integer.toString(i);
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

    private void updateListOfGames(){

        if(gamesScheduled != null) {
            List<String> gamelst = new ArrayList<String>();
            for (int i = 1; i < gamesScheduled.size(); i++) {
                gamelst.add(gamesScheduled.get(i).toString());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.game_row, gamelst);
            gameList.setAdapter(adapter);
        }

    }

    private void savePreferences(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String loadSavedPreferences(String key) {
        return prefs.getString(key, "Invalid Key");
    }


}
