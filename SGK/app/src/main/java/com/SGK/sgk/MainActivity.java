package com.SGK.sgk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String MyPrefs = "MyPrefs";
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1500);
                }
                catch(Exception ex)
                {

                }
                finally{
                    //startActivity(new Intent(MainActivity.this,SignUp.class));
                    if(isOnline())
                    {
                        if(sharedPreferences.getString("logged","").equals("logged")){
                            Intent intent = new Intent(MainActivity.this,Home.class);
                            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else if(sharedPreferences.getString("Delivery","").equals("Delivery"))
                        {
                            Intent intent = new Intent(MainActivity.this,DeliveryAct.class);
                            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else{
                            Intent intent = new Intent(MainActivity.this,SignUp.class);
                            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                    }
                    else{
                        Intent intent = new Intent(MainActivity.this,ErrorConnection.class);
                        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            }
        });

        t1.start();
    }

    @SuppressWarnings("deprecation")
    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info !=null && info.isConnectedOrConnecting()){
            return true;
        }
        else{
            return false;
        }
    }


}
