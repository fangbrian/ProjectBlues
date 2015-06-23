package com.android.projectblues;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.Collections;
import java.util.List;

/**
 * Created by fangb on 12/25/2014.
 */

public class GameAdapter extends BaseAdapter {

    Context context;
    private List<Game> gameItems = Collections.emptyList();

    public GameAdapter(Context context) {
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtOpponent;
        //TextView txtLocation;
        TextView txtTime;
    }

    public void setGameItems(List<Game> gameItems) {
        this.gameItems = gameItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;


        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.game_row, null);
            holder = new ViewHolder();
            holder.txtOpponent = (TextView) convertView.findViewById(R.id.opponent);
            //holder.txtLocation = (TextView) convertView.findViewById(R.id.location);
            holder.txtTime = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        Game currGame = (Game) getItem(position);
        holder.txtOpponent.setText(currGame.getAwayTeam() + " vs. " + currGame.getHomeTeam());
        //holder.txtLocation.setText(currGame.getRinkName());
        holder.txtTime.setText(currGame.getGameTime()+ "@" + currGame.getRinkName());

        return convertView;
    }

    @Override
    public int getCount() {
        return gameItems.size();
    }

    @Override
    public Object getItem(int position) {
        return gameItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return gameItems.indexOf(getItem(position));
    }

    /*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.game_row, null);

        }

        Game currGame = getItem(position);

        if (currGame != null) {

            TextView gameOpponent = (TextView) view.findViewById(R.id.opponent);
            TextView gameLocation = (TextView) view.findViewById(R.id.location);
            TextView gameTime = (TextView) view.findViewById(R.id.time);

            if (gameOpponent != null) {
                gameOpponent.setText(currGame.getAwayTeam() + " vs. " + currGame.getHomeTeam());
            }
            if (gameLocation != null) {

                gameLocation.setText(currGame.getRinkName());
            }
            if (gameTime != null) {

                gameTime.setText(currGame.getGameTime());
            }

        }

        return view;

    }*/
}
