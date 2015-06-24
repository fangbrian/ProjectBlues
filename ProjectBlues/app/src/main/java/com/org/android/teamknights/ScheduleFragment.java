package com.org.android.teamknights;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;

import java.util.List;

import android.util.Log;
import android.widget.TextView;

/**
 * Fragment to display schedule of all hockey games
 *
 * Created by fangb on 12/22/2014.
 */
public class ScheduleFragment extends Fragment {

    private ListView gameList;
    private SharedPreferences prefs;
    private final static String SHARED_PREFS_NAME = "BluesPreferences";
    private List<Game> gamesScheduled = null;
    private final static String NEXT_GAME = "0";
    private final Gson gson = new Gson();
    private GameAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.schedule_fragment, null);
        gameList = (ListView) rootView.findViewById(R.id.gamelist);

//        gameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView <?> parent, View view,
//                                    int position, long id) {
//                Log.i("debug", "single click");
//            }
//        });

        gameList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                TextView gameTeams = (TextView) arg1.findViewById(R.id.opponent);
                TextView gameTime = (TextView) arg1.findViewById(R.id.time);
                Log.i("debug", gameTeams.getText().toString());
                Log.i("debug", gameTime.getText().toString());

                String textMessage = "Next Game: " + gameTeams.getText().toString() + " on " + gameTime.getText().toString();


                Intent sendIntent = new Intent();
                // Set the action to be performed i.e 'Send Data'
                sendIntent.setAction(Intent.ACTION_SEND);
                // Add the text to the intent
                sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
                // Set the type of data i.e 'text/plain'
                sendIntent.setType("text/plain");
                // Launches the activity; Open 'Text editor' if you set it as default app to handle Text
                startActivity(sendIntent);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new GameAdapter(getActivity());
        gameList.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        refreshSchedule();
    }

    private void refreshSchedule() {
        new PointstreakParser() {
            @Override
            protected void onPostExecute(List<Game> games) {
                super.onPostExecute(games);

                // onPostExecute is guaranteed to be called on the GUI thread, so it's safe
                // to call displayText here (if we tried in doInBackground, it will crash)

                if (games != null) {
                    mAdapter.setGameItems(games);
                    mAdapter.notifyDataSetChanged();
                    save(games);
                }
            }
        }.execute();
    }

    // --- SharedPref helpers

    private void clearSavedPreferences(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    public void save(List<Game> games){

        for(int i = 0; i < games.size(); i++) {
            Game currGame = games.get(i);
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

    private void savePreferences(String key, String value) {
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String loadSavedPreferences(String key) {
        return prefs.getString(key, "Invalid Key");
    }


}
