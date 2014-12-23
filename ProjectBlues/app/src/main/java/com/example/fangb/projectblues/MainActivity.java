package com.example.fangb.projectblues;

import android.support.v4.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;




 //TODO: check for internet connectivity
 //TODO: Notification on the day of the game

public class MainActivity extends ActionBarActivity {

    private final Gson gson = new Gson();
    private TextView textView;
    private TextView displayStandings;
    private List<Game> gamesScheduled = null;
    private NotificationManager myNotificationManager;

    private SharedPreferences prefs;
    private final static String SHARED_PREFS_NAME = "BluesPreferences";
    private final static String MY_TEAM = "Knights";
    private final static String NEXT_GAME = "0";
    private ViewPager mPager;
    ActionBar mActionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("TEAM KNIGHTS");
        /** Getting a reference to action bar of this activity */
        mActionbar = getSupportActionBar();

        /** Set tab navigation mode */
        mActionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        /** Getting a reference to ViewPager from the layout */
        mPager = (ViewPager) findViewById(R.id.pager);

        /** Getting a reference to FragmentManager */
        FragmentManager fm = getSupportFragmentManager();

        /** Defining a listener for pageChange */
        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mActionbar.setSelectedNavigationItem(position);
            }
        };

        /** Setting the pageChange listener to the viewPager */
        mPager.setOnPageChangeListener(pageChangeListener);

        /** Creating an instance of FragmentPagerAdapter */
        MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(fm);

        /** Setting the FragmentPagerAdapter object to the viewPager object */
        mPager.setAdapter(fragmentPagerAdapter);

        mActionbar.setDisplayShowTitleEnabled(true);

        /** Defining tab listener */
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }

        };

        /** Creating fragment1 Tab */
        ActionBar.Tab tab = mActionbar.newTab()
                .setText("Schedule")
                .setTabListener(tabListener);

        mActionbar.addTab(tab);

        /** Creating fragment2 Tab */
        tab = mActionbar.newTab()
                .setText("Rankings")
                .setTabListener(tabListener);

        mActionbar.addTab(tab);

        //VIEWPAGER
        //textView = (TextView) findViewById(R.id.textDisplay);
        //displayStandings = (TextView) findViewById(R.id.textStandings);
        prefs = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
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
                    testGatherService();
                    //Gather Standings
                    gatherStandings();
                } else {
                    if(prefs.contains(NEXT_GAME)){
                        updateNextGame();
                    } else {
                        //displayText("Waiting for Internet Connection");
                    }
                }
            }
        };

        //Schedule updates every hour
        timer.schedule (hourlyTask, 0l, 1000*60*60);

    }

/*
    private void initializeNotification(){
        // Invoking the default notification service
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setContentTitle("New Message with explicit intent");
        mBuilder.setContentText("New message from javacodegeeks received");
        mBuilder.setTicker("Explicit: New Message Received!");
        mBuilder.setSmallIcon(R.drawable.ic_launcher);



        // start the activity when the user clicks the notification text

        mBuilder.setContentIntent(resultPendingIntent);

        myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // pass the Notification object to the system
        myNotificationManager.notify(notificationIdOne, mBuilder.build());


    }
    */

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
            //displayText(game.displayGame());

        } else{
            //displayText("Error. No games exist");
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

    private void testGatherService(){
        Intent msgIntent = new Intent(this, WebParser.class);
        startService(msgIntent);
    }

    private void gatherStandings() {

        StandingsParser task = new StandingsParser() {
            @Override
            protected void onPostExecute(String teams) {
                super.onPostExecute(teams);

                // onPostExecute will print the standings

                if(teams != null){
                    //printStandings(teams);
                } else{
                    //printStandings("");
                }
            }
        };
        task.execute();
    }

/* SHERLOCK ACTION BAR
    // TODO: Use SherlockActionBar for backwards compatability
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.my, menu);

        return super.onCreateOptionsMenu(menu);
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

    */

/*
    private void displayText(String message) {
        textView.setText(message);
    }


    private void printStandings(String message){
        displayStandings.setText(message);
    }

    */


}
