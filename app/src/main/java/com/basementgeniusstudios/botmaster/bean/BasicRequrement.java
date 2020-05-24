package com.basementgeniusstudios.botmaster.bean;

import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import models.Action;

public abstract class BasicRequrement {
    public boolean isFromGroup;
    public String groupname;
    public String msg;
    public String orimsg;
    public String sender;
    public String groupdevider=":";//@ or :
    public String package_name;
    public Action action;
    public Context context;
    public String todaysdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());



    public void init(String msg, String sender, String package_name, Action action, Context context) {
        l(sender);
        if(sender.contains(groupdevider)){
            String[] parts =sender.split(groupdevider);
            this.sender=parts[1];
            this.groupname=parts[0];

            isFromGroup=true;

        }else{
            this.sender = sender;
            isFromGroup=false;
        }
        this.package_name=package_name;
        this.msg = msg.toLowerCase();
        this.orimsg = msg;
        this.action=action;
        this.context=context;

        //com.whatsapp

        if(package_name.equals("com.whatsapp")){
            try {
                this.Start();
            } catch (JSONException | IOException | PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }

    }


    public void reply(String text){
        try {
            action.sendReply(context,text);
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    public void savefile(String fl, String jsonString) throws IOException {
        File rootFolder = context.getExternalFilesDir(null);
        File jsonFile = new File(rootFolder, fl);
        FileWriter writer = new FileWriter(jsonFile);
        writer.write(jsonString);
        writer.close();
        //or IOUtils.closeQuietly(writer);

    }
    public String getFile(String filename){
        File rootFolder = context.getExternalFilesDir(null);
        File jsonFile = new File(rootFolder, filename);
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
        return json;

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
    abstract public void Start() throws JSONException, IOException, PendingIntent.CanceledException;
}
