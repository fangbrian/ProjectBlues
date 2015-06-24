package com.example.fangb.projectblues;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fangb on 10/2/2014.
 */
public class StandingsParser extends AsyncTask<Void, Void, List<String>> {

    private static final String URL = "http://www.pointstreak.com/players/players-team.html?teamid=503916";

    @Override
    protected List<String> doInBackground(Void... params) {
        try {
            Document doc  = Jsoup.connect(URL).get();
            Element fields = doc.select("tr.fields").first();

            List<String> teams = parseTeams(fields);

            // Do stuff with games here

            //return "Size of Array List " + games.size();//currentGame.toString();
            return teams;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Parse all game data from td elements in the table that contains the tr.fields Element
     * @param fields - tr.fields Element that is contained in the games table
     */
    private List<String> parseTeams(Element fields) {
        Element curr = fields;
        List<String> team = new ArrayList<String>();
        while (true) {
            Element next = curr.nextElementSibling();
            if (next == null) break;

            String currTeam = parseTeam(next);
            if (currTeam != null) {
                team.add(currTeam);
            }
            curr = next;

        }

        return team;

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
    private String parseTeam(Element next) {
        //Gets Home Team
        Element td = next.select("td").first();
        if(td == null) return null;
        String team = td.select("a").first().text();
        return team;

        //return null;

    }



}

