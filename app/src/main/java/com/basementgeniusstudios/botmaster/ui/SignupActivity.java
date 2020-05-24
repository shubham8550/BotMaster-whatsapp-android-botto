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
import com.basementgeniusstudios.botmaster.api.JSONRequestMaker.JSONFetch;
import com.basementgeniusstudios.botmaster.api.JSONRequestMaker.inc.JSONAdapter;
import com.basementgeniusstudios.botmaster.validators;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    EditText u;
    EditText e;
    EditText p;
    public ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        u=findViewById(R.id.username);
        e=findViewById(R.id.email);
        p=findViewById(R.id.password);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
    }

    public void onpressedsignup(final View view){

        validators v=new validators(SignupActivity.this,e.getText().toString(),u.getText().toString(),p.getText().toString());
        if(v.isValidEmailId() && v.isValidPassword() && v.isValidUserName()){
            ((Button)view).setEnabled(false);
            spinner.setVisibility(View.VISIBLE);
            Map<String,String> param= new HashMap<String, String>();
            param.put("username", u.getText().toString());
            param.put("password",p.getText().toString());
            param.put("email", e.getText().toString());
            JSONFetch req=new JSONFetch(SignupActivity.this,getString(R.string.server_url)+"signup.php",param, Request.Method.POST);
            //req.fetchData();

            req.fetchData(new JSONAdapter() {
                @Override
                public void onDataFateched(JSONObject object) throws JSONException {
                   // Log.d("pokemon",object.toString());
                    //heres your json obj
                    String stat=object.getString("bool");
                    if(stat.equals("true")){
                        //login success
                        Toast.makeText(SignupActivity.this, "Success: Login Into Account", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(SignupActivity.this,LoginActivity.class);
                        startActivity(i);

                    }else {
                        Toast.makeText(SignupActivity.this, object.getString("status"), Toast.LENGTH_SHORT).show();
                        ((Button)view).setEnabled(true);
                        spinner.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(SignupActivity.this, "Unable To Connect: ", Toast.LENGTH_SHORT).show();
                   // Log.d("pokemon",error);
                    ((Button)view).setEnabled(true);
                    spinner.setVisibility(View.GONE);

                }
            });




        }

    }
}
