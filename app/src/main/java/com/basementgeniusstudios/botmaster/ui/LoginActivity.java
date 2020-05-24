package com.basementgeniusstudios.botmaster.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.basementgeniusstudios.botmaster.R;
import com.basementgeniusstudios.botmaster.api.AccountManager;
import com.basementgeniusstudios.botmaster.api.JSONRequestMaker.JSONFetch;
import com.basementgeniusstudios.botmaster.api.JSONRequestMaker.inc.JSONAdapter;
import com.basementgeniusstudios.botmaster.validators;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText u;
    public ProgressBar spinner;
    EditText p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        u=findViewById(R.id.username_lgn);
        p=findViewById(R.id.password_lgn);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
    }
    public void Login_onclic(final View view){
        validators v=new validators(LoginActivity.this,u.getText().toString(),p.getText().toString());
        if( v.isValidPassword() && v.isValidUserName()){
            ((Button)view).setEnabled(false);
            spinner.setVisibility(View.VISIBLE);
            Map<String,String> param= new HashMap<String, String>();
            param.put("username", u.getText().toString());
            param.put("password",p.getText().toString());

            JSONFetch req=new JSONFetch(LoginActivity.this,getString(R.string.server_url)+"signin.php",param, Request.Method.POST);
            //req.fetchData();

            req.fetchData(new JSONAdapter() {
                @Override
                public void onDataFateched(JSONObject object) throws JSONException{
                    // Log.d("pokemon",object.toString());
                    //heres your json obj
                    String stat=object.getString("bool");
                    if(stat.equals("true")){
                        //login success
                        try {
                            savecred(object.getString("token"),object.getString("email"),u.getText().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(LoginActivity.this, "Success:Welcome "+u.getText().toString(), Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(LoginActivity.this,HomeActivity.class);
                        // set the new task and clear flags
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                    }else {
                        Toast.makeText(LoginActivity.this, object.getString("status"), Toast.LENGTH_SHORT).show();
                        ((Button)view).setEnabled(true);
                        spinner.setVisibility(View.GONE);

                    }
                }
                void savecred(String tk,String em,String us) throws IOException, JSONException {
                    AccountManager.setToken(LoginActivity.this,tk);
                    AccountManager.setUsername(LoginActivity.this,us);
                    AccountManager.setEmail(LoginActivity.this,em);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(LoginActivity.this, "Unable To Connect: ", Toast.LENGTH_SHORT).show();
                    // Log.d("pokemon",error);
                    ((Button)view).setEnabled(true);
                    spinner.setVisibility(View.GONE);

                }
            });




        }










    }
    public void gotosignup(View view){
        Intent i=new Intent(LoginActivity.this,SignupActivity.class);
        startActivity(i);
    }
}
