package com.org.android.teamknights;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;

/**
 * Fragment to display rankings of hockey teams
 * Created by fangb on 12/22/2014.
 */
public class RankingFragment extends Fragment {
    private ListView rankingList;
    private RankingsAdapter mAdapter;

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

        mAdapter = new RankingsAdapter(getActivity());
        rankingList.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshRankings();
    }

    private void refreshRankings() {
        new StandingsParser() {
            @Override
            protected void onPostExecute(List<String> teams) {
                super.onPostExecute(teams);

                // onPostExecute will print the standings
                if(teams != null){
                    //Print Standings
                    mAdapter.setRankings(teams);
                    mAdapter.notifyDataSetChanged();
                } else{
                    //printStandings("");
                }
            }
        }.execute();
    }

    private static class RankingsAdapter extends BaseAdapter {
        private final Context mContext;
        private List<String> mRankings = Collections.emptyList();

        public RankingsAdapter(Context context) {
            mContext = context;
        }

        public void setRankings(List<String> rankings) {
            mRankings = rankings;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) mContext
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.rankings_row, parent, false);
            }

            TextView rankingView = (TextView) convertView.findViewById(R.id.team_ranking);
            String ranking = mRankings.get(position);
            rankingView.setText(ranking);

            return convertView;
        }

        @Override
        public int getCount() {
            return mRankings.size();
        }

        @Override
        public Object getItem(int position) {
            return mRankings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }
}
