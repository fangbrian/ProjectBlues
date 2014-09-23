package com.example.fangb.projectblues;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by fangb on 9/17/2014.
 */
public class PointstreakParser extends AsyncTask<Void, Void, String> {

    private static final String URL = "http://www.pointstreak.com/players/players-division-schedule.html?divisionid=75990&seasonid=12867";

    @Override
    protected String doInBackground(Void... params) {
        try {
            Document doc  = Jsoup.connect(URL).get();
            Element tr = doc.select("tr.fields").first();

            return tr.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
