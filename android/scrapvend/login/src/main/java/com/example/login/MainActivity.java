package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.DatabaseConnection.MySqlConnector;
import com.example.login.login.registration_user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
private Button sign_up,login;
public EditText _email,_password;
public String email,password;
public CheckBox _remember;
public TextView _forgetp;
SharedPreferences sp,pickupersonsp;
private static String tag="sp";
public static String user;
    Intent in,inp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        in = new Intent(MainActivity.this,User.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

        inp = new Intent(MainActivity.this,PickupPerson.class);
        inp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);


        _email = (EditText) findViewById(R.id.email);
        _password = (EditText) findViewById(R.id.password);
        _remember = (CheckBox) findViewById(R.id.remember);
        _forgetp = (TextView) findViewById(R.id.forgetp);

        login = (Button) findViewById(R.id.login);
        sp=getSharedPreferences("login", Context.MODE_PRIVATE);
        pickupersonsp=getSharedPreferences("login", Context.MODE_PRIVATE);

        if((sp.contains("username"))){
            Log.d(tag,sp.getString("username","null"));
            user=sp.getString("username",null);
            startActivity(in);
        }
        if((pickupersonsp.contains("PickupPersonUsername"))){
            Log.d(tag,pickupersonsp.getString("username","null"));
            startActivity(inp);
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = _email .getText().toString();
                password = _password.getText().toString();
                new CheckLogin().execute();
            }

        });

        sign_up=(Button) findViewById(R.id.sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openregistration();
            }

            public void openregistration(){
                Intent intent = new Intent(MainActivity.this, registration_user.class);
                startActivity(intent);
            }});

    }

         class CheckLogin extends AsyncTask<String, String, String> {
            String z = "";
            Boolean isSuccess = false;

            @Override
            protected void onPostExecute(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                if (isSuccess) {
                    Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... strings) {

                MySqlConnector connector = new MySqlConnector();
                Connection connection = connector.getMySqlConnection();

                if (email.trim().equals("") || password.trim().equals("")) {
                    z = "Please enter the username and password";
                } else {
                    try {

                        if (connection == null) {
                            z = "Check your internet Connection";
                        } else {
                            String query = "SELECT * FROM `login_info` WHERE `Username`=\"" + email + "\" and `password`=\"" + password + "\"";
                            PreparedStatement prest = connection.prepareStatement(query);
                            ResultSet rs = prest.executeQuery(query);
                            if (rs.next()) {
                                int role = rs.getInt(2);
                                if (role == 2) {
                                    z = "Login Successful...";
                                    isSuccess = true;
                                    SharedPreferences.Editor e = sp.edit();
                                    e.putString("username", email);
                                    e.putString("password", password);
                                    e.commit();
                                    user=sp.getString("username",null);
                                    Log.d(tag, sp.getString("username", "null"));

                                    Log.d(tag,"user name"+ user);
                                    startActivity(in);

                                }
                                if (role == 1) {
                                    z = "Login Successful...";
                                    isSuccess = true;
                                    SharedPreferences.Editor ep = pickupersonsp.edit();
                                    ep.putString("PickupPersonUsername", email);
                                    ep.putString("password", password);
                                    ep.commit();
                                    Log.d(tag, pickupersonsp.getString("username", "null"));
                                    startActivity(inp);

                                }
                            } else {
                                z = "Invalid Credentials...";
                                isSuccess = false;
                            }


                        }
                    } catch (SQLException e) {
                        z = e.getMessage();
                    } catch (Exception e) {
                        isSuccess = false;
                        z = e.getMessage();
                    } finally {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                }

                return z;
            }
        }


}
