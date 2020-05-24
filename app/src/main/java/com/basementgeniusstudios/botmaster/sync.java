package com.basementgeniusstudios.botmaster;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.basementgeniusstudios.botmaster.api.AccountManager;
import com.basementgeniusstudios.botmaster.api.JSONRequestMaker.JSONFetch;
import com.basementgeniusstudios.botmaster.api.JSONRequestMaker.inc.JSONAdapter;
import com.basementgeniusstudios.botmaster.ui.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class sync extends Thread{
   final public static int frequency=4;
    private Context context;
    private String server_url;

    sync(Context context,String server_url){

        this.context = context;
        this.server_url = server_url;
    }

    @Override
    public void run() {
        super.run();
        if(AccountManager.accFileExist(context)){
            try {
                AccountManager.init(context);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            return;
        }


                Map<String,String> param= new HashMap<String, String>();
        param.put("username", AccountManager.getUsername());
        param.put("token", AccountManager.getToken());
        //Log.d("pokemon",param.toString());

        JSONFetch req=new JSONFetch(context,server_url+"sync.php",param,Request.Method.POST);
        //req.fetchData();
        req.fetchData(new JSONAdapter() {
            @Override
            public void onDataFateched(JSONObject object) throws JSONException {
                if(object.getString("status").equals("active")){
                    //Plan is active keep session and reset msg stack
                    AccountManager.isExpired=false;

                }else if(object.getString("status").equals("expired")){
                    //Plan expired or noplan remove old plan session
                    AccountManager.isExpired=true;
                }else if(object.getString("relogin").equals("true")){
                    //force login and session destroy
                    AccountManager.isExpired=true;
                    AccountManager.deleteAccFile(context);

                }

            }

            @Override
            public void onError(String error) {
                Log.d("pokemon","SYNC Failed "+error);
            }
        });


    }
}
