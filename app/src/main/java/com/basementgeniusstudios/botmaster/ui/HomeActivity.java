package com.basementgeniusstudios.botmaster.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.ListView;

import com.basementgeniusstudios.botmaster.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

public class HomeActivity extends AppCompatActivity {

    private ListView configLisV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        configLisV = findViewById(R.id.configsListView);


    }
    public void openNotificationSettings(View view) {
        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
    }
    public void openBatterOpt(View view) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }
    }

    JSONObject DumpData() throws JSONException {
        JSONObject base=new JSONObject();

        JSONObject data1=new JSONObject();
        data1.put("rule","Contains");
        data1.put("query","hii");
        data1.put("reply","how r u");

        JSONObject data2=new JSONObject();
        data2.put("rule","StartsWith");
        data2.put("query","/help");
        data2.put("reply","StartWith");


        JSONArray arr=new JSONArray();
        arr.put(data1);
        arr.put(data2);

        base.put("quries",arr);
        return base;
        //base.getJSONArray("quries").getJSONObject(0).getString("reply");

    }
}
