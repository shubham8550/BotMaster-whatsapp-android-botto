package com.basementgeniusstudios.botmaster.api.JSONRequestMaker.inc;

import org.json.JSONException;
import org.json.JSONObject;

public interface JSONAdapter
{
    public void onDataFateched(JSONObject object) throws JSONException;
    public void onError(String error);

}