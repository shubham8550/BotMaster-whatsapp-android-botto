package com.basementgeniusstudios.botmaster.api;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class AccountManager {
    public static boolean isExpired=true;
    private static String username;
    private static String token;
    private static String email;
    private static String saveFileName="loading.gif";
    public static void setUsername(Context context,String username) throws JSONException, IOException {
        JSONObject data=getJSONObjectFromFileName(context,saveFileName);
        if(data==null){
            data=new JSONObject();
        }
        data.put("username",username);
        savefile(context,saveFileName,data.toString());
        AccountManager.username = username;
    }

    public static void setToken(Context context,String token) throws IOException, JSONException {
        JSONObject data=getJSONObjectFromFileName(context,saveFileName);
        if(data==null){
            data=new JSONObject();
        }

        data.put("token",token);
        savefile(context,saveFileName,data.toString());
        AccountManager.token = token;
    }

    public static void setEmail(Context context,String email) throws IOException, JSONException {
        JSONObject data=getJSONObjectFromFileName(context,saveFileName);
        if(data==null){
            data=new JSONObject();
        }
        data.put("email",email);
        savefile(context,saveFileName,data.toString());
        AccountManager.email = email;
    }
    public static void init(Context context) throws JSONException {
        JSONObject data=getJSONObjectFromFileName(context,saveFileName);
        username= data.getString("username");
        token= data.getString("token");
        email= data.getString("email");
        //Log.d("pokemon","INIT : "+data.toString());

    }


    public static String getUsername() {
        return username;
    }

    public static String getToken() {
        return token;
    }

    public static String getEmail() {
        return email;
    }


    private static JSONObject getJSONObjectFromFileName(Context context, String ffname){
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

        }
        return null;
    }
    public static boolean accFileExist(Context context){
        File rootFolder = context.getExternalFilesDir(null);
        File jsonFile = new File(rootFolder, saveFileName);

        if(jsonFile.exists()){
            return true;
        }
        return false;
    }
    public static void deleteAccFile(Context context){
        File rootFolder = context.getExternalFilesDir(null);
        File jsonFile = new File(rootFolder, saveFileName);

        if(jsonFile.exists()){
            boolean deleted = jsonFile.delete();
            Log.v("pokemon","Acc file deleted: " + deleted);
        }

    }
    private static void savefile(Context context,String fl, String jsonString) throws IOException {
        File rootFolder = context.getExternalFilesDir(null);
        File jsonFile = new File(rootFolder, fl);
        FileWriter writer = new FileWriter(jsonFile);
        writer.write(jsonString);
        writer.close();
        //or IOUtils.closeQuietly(writer);

    }

}
