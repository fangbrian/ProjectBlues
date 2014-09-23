package com.example.fangb.projectblues;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by fangb on 9/17/2014.
 */
public class PointstreakParse extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        // The url we passed into execute will be given as the first element in the strings array
        String url = strings[0];

        try {
            // We need to call Jsoup on another thread, or it'll crash
            Document doc  = Jsoup.parse(url);
            Elements gameElements = doc.select("tr.fields");

            return doc.title();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;

    }
}
