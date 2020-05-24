package com.basementgeniusstudios.botmaster.api.JSONRequestMaker;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.basementgeniusstudios.botmaster.api.JSONRequestMaker.inc.JSONAdapter;
import com.basementgeniusstudios.botmaster.api.JSONRequestMaker.inc.JSONBoss;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JSONFetch {

    private final Context context;
    private final String url;
    private final Map<String, String> params;
    private final int requestType;
    public JSONFetch(Context context, String url, Map<String, String> params,int requestType){

        this.context = context;
        this.requestType = requestType;
        this.url = url;
        this.params = params;
    }
    public void fetchData(JSONAdapter adapter){
        new JSONBoss(adapter,context,url,params, requestType);
    }

//    void sampleRequest(){
//        Map<String,String> param= new HashMap<String, String>();
//        param.put("username", " shubham");
//        JSONFetch req=new JSONFetch(context,"google.com.api",param,Request.Method.GET);
//        //req.fetchData();
//
//        req.fetchData(new JSONAdapter() {
//            @Override
//            public void onDataFateched(JSONObject object) throws JSONException {
//                //heres your json obj
//            }
//
//            @Override
//            public void onError(String error) {
//
//            }
//        });
//
//
//    }

}
