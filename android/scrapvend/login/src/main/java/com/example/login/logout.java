package com.example.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class logout extends AppCompatActivity {

    private static String tag="sp";
    @Override
    protected void onCreate(final Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        SharedPreferences sp=getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor e=sp.edit();
        e.clear();
        e.commit();
        SharedPreferences pickuppersonsp=getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor ep=pickuppersonsp.edit();
        ep.clear();
        ep.commit();

        Log.d(tag,sp.getString("username","null"));
        Intent in = new Intent(this,MainActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        finish();

    }
}
