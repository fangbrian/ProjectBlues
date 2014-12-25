package com.example.fangb.projectblues;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fangb on 12/22/2014.
 */
public class RankingFragment extends Fragment {
    private ListView rankingList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.schedule_fragment, null);
        rankingList = (ListView) rootView.findViewById(R.id.gamelist);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        parseHtmlRankings();


    }

    private void parseHtmlRankings(){
        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {

                if(isNetworkAvailable()) {
                    //Gather Standings
                    gatherStandings();
                } else {

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

    private void gatherStandings() {

        StandingsParser task = new StandingsParser() {
            @Override
            protected void onPostExecute(List<String> teams) {
                super.onPostExecute(teams);

                // onPostExecute will print the standings

                if(teams != null){
                    //Print Standings
                    List<String> rankinglst = new ArrayList<String>();
                    for (int i = 0; i < teams.size(); i++) {
                        rankinglst.add(teams.get(i));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            R.layout.game_row, rankinglst);
                    rankingList.setAdapter(adapter);
                } else{
                    //printStandings("");
                }
            }
        };
        task.execute();
    }


}
