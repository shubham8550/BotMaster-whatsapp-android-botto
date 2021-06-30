package com.basementgeniusstudios.botmaster.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.basementgeniusstudios.botmaster.R;
import com.basementgeniusstudios.botmaster.api.AccountManager;
import com.basementgeniusstudios.botmaster.config.Res;
import com.basementgeniusstudios.botmaster.config.conf;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

public class HomeActivity extends AppCompatActivity {

    Button modify_rule_button;
    Switch linkwarnswt;
    Switch pollswt;
    Switch animangaswt;
    Switch gameswt;
    Spinner ruleSpinner;
    EditText rulereply;
    EditText rulequery;
    EditText admintextip;
    Button rulesavebtn;
    Button adminsavebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ruleSpinner=findViewById(R.id.ruleSpinner);
        rulequery=findViewById(R.id.ruleQuery);
            rulereply=findViewById(R.id.ruleReply);
        rulesavebtn=findViewById(R.id.ruleAddbtn);
        isforgrpswt=findViewById(R.id.isForGroupSwitch);

        adminsavebtn=findViewById(R.id.adminsavebutton);
        admintextip=findViewById(R.id.adminsEdittext);
        linkwarnswt=findViewById(R.id.linkwarnSwitch);
        pollswt=findViewById(R.id.pollSwitch);
        animangaswt=findViewById(R.id.animemangaSwitch);
        gameswt=findViewById(R.id.snakeLadderSwitch);
        //switches init
        try {
            switchInit();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        modify_rule_button = findViewById(R.id.modify_rule_btn);

        modify_rule_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent modify_rule_intent = new Intent(HomeActivity.this, CustomCommandsActivity.class);
                startActivity(modify_rule_intent);
            }
        });
    }
    Switch isforgrpswt;
    public void addRuleonclick(View view) throws JSONException, IOException {

        if(TextUtils.isEmpty(rulequery.getText().toString()) || TextUtils.isEmpty(rulereply.getText().toString())){
            Toast.makeText(this, "Invalid Values", Toast.LENGTH_SHORT).show();
            return;
        }


        JSONObject data=getJSONObjectFromFileName(getString(R.string.customRulesFile));


        JSONObject data1=new JSONObject();
        data1.put("rule",ruleSpinner.getSelectedItem().toString());
        data1.put("query",rulequery.getText().toString());
        data1.put("reply",rulereply.getText().toString());
        data1.put("isForGroups",isforgrpswt.isChecked());
        Toast.makeText(this, data1.toString(), Toast.LENGTH_SHORT).show();
//
        if(data==null){
            data=new JSONObject();
            data.put("quries",new JSONArray());
        }
        data.getJSONArray("quries").put(data1);
        savefile(getString(R.string.customRulesFile),data.toString());
        Toast.makeText(this, "Rule Added Successfully", Toast.LENGTH_SHORT).show();
        rulereply.setText("");
        rulequery.setText("");



    }


    private void switchInit() throws JSONException {
        final conf con=new conf(HomeActivity.this);

        admintextip.setText(con.get(Res.admins));
        linkwarnswt.setChecked( (con.get(Res.warnHTTP).equals("true"))? true:false );
        pollswt.setChecked( (con.get(Res.poll).equals("true"))? true:false );
        animangaswt.setChecked( (con.get(Res.animeSearch).equals("true"))? true:false );
        gameswt.setChecked( (con.get(Res.game).equals("true"))? true:false );


        adminsavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new conf(HomeActivity.this).add(Res.admins,admintextip.getText().toString());
                    Toast.makeText(HomeActivity.this, "Save Success", Toast.LENGTH_SHORT).show();
                } catch (JSONException | IOException e) {
                    Toast.makeText(HomeActivity.this, "Save Failed", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            }
        });
        linkwarnswt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                try {
                    con.add(Res.warnHTTP,String.valueOf(isChecked));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        pollswt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                try {
                    con.add(Res.poll,String.valueOf(isChecked));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        animangaswt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                try {
                    con.add(Res.animeSearch,String.valueOf(isChecked));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        gameswt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                try {
                    con.add(Res.game,String.valueOf(isChecked));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void logout(View v){
        return;
//        AccountManager.isExpired=true;
//        AccountManager.deleteAccFile(HomeActivity.this);
//        try{
//            Intent i=new Intent(HomeActivity.this,LoginActivity.class);
//            HomeActivity.this.startActivity(i);
//            Toast.makeText(this, "Sign out Successfully", Toast.LENGTH_SHORT).show();
//        }catch (Exception e){
//            Log.d("pokemon","LOGOUT");
//        }
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

    //extras
    public JSONObject getJSONObjectFromFileName(String ffname){
        File rootFolder = this.getExternalFilesDir(null);
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
            //l("Error on converting text to json obj in getJSONObjectFromFileName from file "+ffname);
        }
        return null;
    }
    public boolean isFileExist(String filss){
        File rootFolder = this.getExternalFilesDir(null);
        File jsonFile = new File(rootFolder, filss);

        if(jsonFile.exists()){
            return true;
        }
        return false;
    }
    public void savefile(String fl, String jsonString) throws IOException {
        File rootFolder = this.getExternalFilesDir(null);
        File jsonFile = new File(rootFolder, fl);
        FileWriter writer = new FileWriter(jsonFile);
        writer.write(jsonString);
        writer.close();
        //or IOUtils.closeQuietly(writer);

    }
}
