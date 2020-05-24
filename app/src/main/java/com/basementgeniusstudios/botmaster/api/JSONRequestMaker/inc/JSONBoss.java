package com.basementgeniusstudios.botmaster.api.JSONRequestMaker.inc;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basementgeniusstudios.botmaster.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class JSONBoss {


    public JSONBoss(final JSONAdapter adapter, Context context, String url, final Map<String, String> params,int RequestType ) {



        RequestQueue MyRequestQueue = Volley.newRequestQueue(context);



        StringRequest MyStringRequest = new StringRequest(RequestType, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                //This code is executed if the server responds, whether or not the response contains data.

               // Log.d("pokemon","Response : "+response);
                try {
                    adapter.onDataFateched(new JSONObject(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                    adapter.onError("JSONParsing Error : "+e.toString());

                }
                //The String 'response' contains the server's response.


            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                adapter.onError("VollyERRR :"+error.toString());
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
               // Map<String, String> MyData = new HashMap<String, String>();

                //  MyData.put("data", ""); //Add the data you'd like to send to the server.
                //return MyData;
                return params;
            }
        };

        MyRequestQueue.add(MyStringRequest);

    }


}