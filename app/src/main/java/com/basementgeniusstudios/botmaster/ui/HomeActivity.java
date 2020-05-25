package com.basementgeniusstudios.botmaster.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.basementgeniusstudios.botmaster.R;
import com.basementgeniusstudios.botmaster.api.AccountManager;
import com.basementgeniusstudios.botmaster.config.Res;
import com.basementgeniusstudios.botmaster.config.conf;

import org.json.JSONException;

import java.io.IOException;

import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

public class HomeActivity extends AppCompatActivity {

    Button modify_rule_button;
    Switch linkwarnswt;
    Switch pollswt;
    Switch animangaswt;
    Switch gameswt;

    EditText admintextip;
    Button adminsavebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
        AccountManager.isExpired=true;
        AccountManager.deleteAccFile(HomeActivity.this);
        try{
            Intent i=new Intent(HomeActivity.this,LoginActivity.class);
            HomeActivity.this.startActivity(i);
            Toast.makeText(this, "Sign out Successfully", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.d("pokemon","LOGOUT");
        }
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
}
