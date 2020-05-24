package com.basementgeniusstudios.botmaster.bean;

import android.app.PendingIntent;

import org.json.JSONException;

import java.io.IOException;

public interface BasicStructure {
    public void Start() throws JSONException, IOException, PendingIntent.CanceledException;
}
