package com.org.android.teamknights;

import android.util.Log;

import java.util.Date;

/**
 * Created by fangb on 9/6/2014.
 */
public class Game {
    private String homeTeam;
    private String awayTeam;
    private Date gameTime;
    private String rinkName;

    public Game(String home, String away, Date gameDate, String rinkLoc){
        homeTeam = home;
        awayTeam = away;
        gameTime = gameDate;
        rinkName = rinkLoc;
    }

    public String getHomeTeam(){
        return homeTeam;
    }

    public String getAwayTeam(){
        return awayTeam;
    }

    public String getRinkName() { return rinkName; }

    public String getGameTime() {
        String gameString = gameTime.toString();
        return gameString.substring(0,20) + "PST"
                + gameString.substring(23);
        //return gameTime.toString();
        }

    public String displayGame(){
        String display = "Next Game: " + gameTime.toString() + " " + homeTeam + " vs. " + awayTeam + " @ "
                + rinkName;
        return display;
    }

    @Override
    public String toString(){
        return "Game [homeTeam = " + homeTeam + ", awayTeam = " + awayTeam
                + ", Date = " + gameTime.toString() + ", rinkName = " + rinkName + "]";
    }

}
