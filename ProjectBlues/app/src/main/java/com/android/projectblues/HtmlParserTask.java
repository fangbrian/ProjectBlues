package com.android.projectblues;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by isyi on 9/16/2014.
 */
public class HtmlParserTask extends AsyncTask<String, Void, String> {

    /**
     * doInBackground will get called on a separate thread and should perform work that isn't
     * good to perform on the GUI thread.  It is NOT safe to manipuate views in this method.
     * @param strings
     * @return
     */
    @Override
    protected String doInBackground(String... strings) {
        // The url we passed into execute will be given as the first element in the strings array
        String url = strings[0];

        try {
            // We need to call Jsoup on another thread, or it'll crash
            Document doc  = Jsoup.connect(url).get();

            return doc.title();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * onPostExecute will get called on the GUI thread and will be provided with the
     * return value from doInBackground.  It is safe to manipulate views in this method
     * @param urlTitle
     */
    @Override
    protected void onPostExecute(String urlTitle) {
        super.onPostExecute(urlTitle);
    }

    public void parseUrl(String url) {
        // Execute will run this AsyncTask and start the doInBackground method
        execute(url);
    }
}