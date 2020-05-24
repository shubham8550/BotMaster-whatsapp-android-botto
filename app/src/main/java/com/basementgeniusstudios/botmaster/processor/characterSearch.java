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

public class characterSearch extends Thread {
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
        l("Charcter Searching started :" + q);
        JSONObject json = null;
        try {
            json = readJsonFromUrl("https://api.jikan.moe/v3/search/character?q="+q+"&limit=1");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
      if(json==null){
          l("Empty character search");
          reply("*Well I dont Know Who is "+q+"* ");
          return;
      }
        try {

            replyer(json.getJSONArray("results").getJSONObject(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void replyer(JSONObject obj) throws JSONException {
        //l(obj.toString());
        String anli="";

        for (int i = 0; i < obj.getJSONArray("anime").length(); i++) {
            anli=anli+"  *-* "+((JSONObject) obj.getJSONArray("anime").get(i)).get("name")+"\n";
        }

        String mang="";

        for (int i = 0; i < obj.getJSONArray("manga").length(); i++) {
            mang=mang+"  *-* "+((JSONObject) obj.getJSONArray("manga").get(i)).get("name")+"\n";
        }

        String op= "*Anime Guild of Maharashtra*\n"+
                "Character Search Result:-"+"\n\n"+
                "*Name : "+obj.getString("name")+"*\n---- *Anime* ----\n"+
                anli+"*\n---- *Manga* ----\n"+mang+
                "\n*MAL URL* : "+obj.getString("url")+"\n\n" +
                "*Image* : "+obj.getString("image_url")+"\n";
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
    public characterSearch(String q, Action action, Context context) {
        this.context=context;
        this.action=action;
        this.q=q;

    }
}