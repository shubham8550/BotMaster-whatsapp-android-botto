package com.basementgeniusstudios.botmaster;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Pattern;

public class validators {
    private Context context;
    private String email;
    private final String username;
    private final String password;

    public validators(Context context,String email,String username,String password){
       // Toast.makeText(context, email, Toast.LENGTH_SHORT).show();
        this.context = context;
        this.email = email;
        this.username = username;
        this.password = password;
    }
    public validators(Context context,String username,String password){

        this.context = context;

        this.username = username;
        this.password = password;
    }
    public boolean isValidEmailId(){



        boolean b= email.contains("@");
        if(!b){
            Toast.makeText(context, "Invalid Email", Toast.LENGTH_SHORT).show();

        }
        return b;
    }
    public boolean isValidUserName(){
        int minsize=3;
        boolean b=username.length() > minsize;
        if(!b){
            Toast.makeText(context, "Username must be more than "+minsize+" Characters", Toast.LENGTH_SHORT).show();

        }
        return b;
    }

    public boolean isValidPassword(){

        int minsize=4;
        boolean b=password.length() > minsize;
        if(!b){
            Toast.makeText(context, "Password must be more than "+minsize+" Characters", Toast.LENGTH_SHORT).show();

        }
        return b;
    }
}
