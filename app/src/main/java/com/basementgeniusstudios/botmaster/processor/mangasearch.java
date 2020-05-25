package com.basementgeniusstudios.botmaster.processor;


import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import models.Action;

public class mangasearch extends Thread {
    Action action;
    Context context;
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            System.out.println(jsonText);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    @Override
    public void run() {
        super.run();
        JSONObject json = null;
        try {
            json = readJsonFromUrl("https://api.jikan.moe/v3/search/manga?q="+q+"&limit=1");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        if(json==null){
            l("Empty Manga search");
            return;
        }
        try {

            replyer(json.getJSONArray("results").getJSONObject(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void replyer(JSONObject obj) throws JSONException {
        l(obj.getString("synopsis"));

        String op= "*BotMasterK12[^-^]*\n"+
                "Manga Search Result:-"+"\n\n"+
                "*Title : "+obj.getString("title")+"*\n"+
                "Publishing : "+obj.getString("publishing")+"\n"+
                "Score: *"+obj.getString("score")+"*  Chapters: *"+obj.getString("chapters")+"*\n"+
                "*Synopsis* : "+obj.getString("synopsis")+"\n"+"\n"+obj.getString("url")+"\n\n" +
                "Image : "+obj.getString("image_url")+"\n";
        reply(op);
    }
    void reply(String text){
        try {
            action.sendReply(context,text);
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
    void l(String ll){
        Log.d("pokemon",ll);
    }
    String q;
    public mangasearch(String q, Action action, Context context) {
        this.context=context;
        this.action=action;
        this.q=q;

    }
}