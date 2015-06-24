package com.example.fangb.projectblues;

import android.app.IntentService;
import android.content.Intent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fangb on 10/21/2014.
 */
public class WebParser extends IntentService {

    private static final String URL = "http://www.pointstreak.com/players/players-team-schedule.html?teamid=503916";
    //"http://www.pointstreak.com/players/players-division-schedule.html?divisionid=75990&seasonid=12867";

    public WebParser() {
        super("WebParser");
    }

    @Override
    protected void onHandleIntent(Intent intent)  {
        try {
            Document doc  = Jsoup.connect(URL).get();
            Element fields = doc.select("tr.fields").first();

            List<Game> games = parseGames(fields);

            // Do stuff with games here

            //return "Size of Array List " + games.size();//currentGame.toString();
            //return games;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //return null;
    }

    /**
     * Parse all game data from td elements in the table that contains the tr.fields Element
     * @param fields - tr.fields Element that is contained in the games table
     */
    private List<Game> parseGames(Element fields) {
        List<Game> games = new ArrayList<Game>();
        Element curr = fields;

        while (true) {
            Element next = curr.nextElementSibling();
            if (next == null) break;

            Game game = parseGame(next);
            if (game != null) {
                games.add(game);
                //Debug
                //currentGame = game;
                //break;
            }
            curr = next;
        }

        return games;
    }

    /**
     * Parses the td elements to create and return a Game object.  The td elements are as follows:
     * 1. Home team name
     * 2. Away team name
     * 3. Date
     * 4. Time
     *
     * For tr rows that don't have at least 4 td elements, discard row and return null
     * @param next - tr element that is the parent of the td elements
     * @return
     */
    private Game parseGame(Element next) {
        //Gets Home Team
        Element td = next.select("td").first();
        String homeTeam = td.select("a").first().text();

        //Gets Away Team
        td = td.nextElementSibling();
        //Check for end of table
        if(td != null) {
            String awayTeam = td.select("a").first().text();
            //Gets Date
            td = td.nextElementSibling();
            String dateString = td.text();
            //Gets Time
            td = td.nextElementSibling();
            String timeString = td.text();
            //Gets Rink
            td = td.nextElementSibling();
            String rinkName = td.select("a").first().text();

            //Check if Game is over and if so do not add particular game
            if (!rinkName.equals("final")) {
                Date gameDate = convertDate(dateString, timeString);
                Game currentGame = new Game(homeTeam, awayTeam, gameDate, rinkName);
                return currentGame;
            }
        }

        return null;

    }

    private Date convertDate(String dateString, String timeString){
        String monthString;
        int monthInt;
        int begin = dateString.indexOf(",");
        monthString = dateString.substring(begin+2,begin+5);
        monthInt = getMonth(monthString);

        //Get the Day
        begin = dateString.length() - 2;
        String day = dateString.substring(begin, dateString.length());
        int dayInt = Integer.parseInt(day);

        int gameYear;
        Date currDate = new Date();
        gameYear = currDate.getYear();
        if(monthInt < currDate.getMonth() && monthInt >= 0 ){
            gameYear += 1;
        }

        //Parse Date TODO:Clean this up
        String hourString = timeString.substring(0, timeString.indexOf(":"));
        begin = timeString.indexOf(":");
        String minuteString = timeString.substring(begin+1, begin+3);
        begin = timeString.lastIndexOf(" ");
        String afternoon = timeString.substring(begin+1, timeString.length());
        int hour = Integer.parseInt(hourString);
        if(afternoon.equals("pm") && hour < 12){
            hour += 12;
            hour = hour%24;
        }
        if(afternoon.equals("am") && hour == 12) hour = 0;

        Date gameDate = new Date(gameYear, monthInt, dayInt, hour, Integer.parseInt(minuteString));
        return gameDate;
    }

    private int getMonth(String month){
        if(month.equals("Jan")){ return 0;}
        else if(month.equals("Feb")){ return 1;}
        else if(month.equals("Mar")){ return 2;}
        else if(month.equals("Apr")){ return 3;}
        else if(month.equals("May")){ return 4;}
        else if(month.equals("Jun")){ return 5;}
        else if(month.equals("Jul")){ return 6;}
        else if(month.equals("Aug")){ return 7;}
        else if(month.equals("Sep")){ return 8;}
        else if(month.equals("Oct")){ return 9;}
        else if(month.equals("Nov")){ return 10;}
        else if(month.equals("Dec")){ return 11;}
        else {return 0;}
    }
}
