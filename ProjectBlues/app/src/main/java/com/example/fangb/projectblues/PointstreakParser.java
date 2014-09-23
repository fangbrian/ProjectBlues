package com.example.fangb.projectblues;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangb on 9/17/2014.
 */
public class PointstreakParser extends AsyncTask<Void, Void, String> {

    private static final String URL = "http://www.pointstreak.com/players/players-division-schedule.html?divisionid=75990&seasonid=12867";

    @Override
    protected String doInBackground(Void... params) {
        try {
            Document doc  = Jsoup.connect(URL).get();
            Element fields = doc.select("tr.fields").first();

            List<Game> games = parseGames(fields);

            // Do stuff with games here
            
            return "Number of games loaded: " + games.size();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
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
            if (game != null) games.add(game);
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
        return null;
    }
}
