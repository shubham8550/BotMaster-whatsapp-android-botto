package com.basementgeniusstudios.botmaster.config;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class conf {
    private static String confFileName="SystemConfiguration.bin";
    private Context context;

    public conf(Context context){

        this.context = context;
    }
    public void add( String key, String value) throws JSONException, IOException {
        JSONObject data=getJSONObjectFromFileName(confFileName);
        if(data==null){
            data=new JSONObject();
        }
        data.put(key,value);
        savefile(confFileName,data.toString());

    }
    public String get(String key) throws JSONException {
        JSONObject data=getJSONObjectFromFileName(confFileName);
        if(data==null){
            data=new JSONObject();
            return null;
        }
        return data.getString(key);

    }
    public void savefile(String fl, String jsonString) throws IOException {
        File rootFolder = context.getExternalFilesDir(null);
        File jsonFile = new File(rootFolder, fl);
        FileWriter writer = new FileWriter(jsonFile);
        writer.write(jsonString);
        writer.close();
        //or IOUtils.closeQuietly(writer);

    }

    public JSONObject getJSONObjectFromFileName(String ffname){
        File rootFolder = context.getExternalFilesDir(null);
        File jsonFile = new File(rootFolder, ffname);
        String json = null;
        try {
            InputStream is = new FileInputStream(jsonFile);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            l("Error on converting text to json obj in getJSONObjectFromFileName from file "+ffname);
        }
        return null;
    }
    public void l(String ll){
        Log.d("pokemon",ll);
    }
}
