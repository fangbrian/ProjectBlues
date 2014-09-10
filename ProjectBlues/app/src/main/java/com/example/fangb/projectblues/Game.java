package com.example.fangb.projectblues;

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

    @Override
    public String toString(){
        return "Game [homeTeam = " + homeTeam + ", awayTeam = " + awayTeam
                + ", Date = " + gameTime.toString() + ", rinkName = " + rinkName + "]";
    }

}
