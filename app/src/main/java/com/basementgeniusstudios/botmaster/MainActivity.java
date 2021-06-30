package com.basementgeniusstudios.botmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.basementgeniusstudios.botmaster.api.AccountManager;
import com.basementgeniusstudios.botmaster.config.Res;
import com.basementgeniusstudios.botmaster.config.conf;
import com.basementgeniusstudios.botmaster.ui.HomeActivity;
import com.basementgeniusstudios.botmaster.ui.LoginActivity;
import com.basementgeniusstudios.botmaster.ui.SignupActivity;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init
        try {
            initer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Experimentle
        Intent i=new Intent(MainActivity.this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(i);


//
//       new sync(MainActivity.this,getText(R.string.server_url).toString()).start();
//        //session retrive
//        if(AccountManager.accFileExist(MainActivity.this)){
//            try {
//                AccountManager.init(MainActivity.this);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            Intent i=new Intent(MainActivity.this, HomeActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//            startActivity(i);
//        }else {
//            Intent i=new Intent(MainActivity.this, LoginActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(i);
//        }

    }

    private void initer() throws IOException, JSONException {
        //conf INIT
        conf config=new conf(MainActivity.this);
        if(!config.accConfigExist()){
            //if config not Exist
            config.add(Res.animeSearch,"false");
            config.add(Res.poll,"false");
            config.add(Res.game,"false");
            config.add(Res.warnHTTP,"false");
            config.add(Res.admins,"admins");

        }

    }

    public void signup(View v){
        Intent i=new Intent(MainActivity.this, SignupActivity.class);
        startActivity(i);
    }
    public void login(View v){
        Intent i=new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
