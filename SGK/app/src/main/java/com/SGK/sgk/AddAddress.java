package com.SGK.sgk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class AddAddress extends AppCompatActivity {

    ArrayList<Integer> pidlist,quantlist,pricelist,totallist;
    String address="";
    String gtot;
    int grandtotal;
    EditText et1,et2,et3,et4,et5;
    Button btn1;
    SharedPreferences sharedPreferences;
    private final String MyPrefs = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        sharedPreferences = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);

        //Toast.makeText(this, sharedPreferences.getString("Id",""), Toast.LENGTH_SHORT).show();

        et1 = findViewById(R.id.etadline1);
        et2 = findViewById(R.id.etadline2);
        et3 = findViewById(R.id.etlandmark);
        et4 = findViewById(R.id.etcity);
        et5 = findViewById(R.id.etpincode);
        btn1 = findViewById(R.id.btnsaveAddress);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String l1 = et1.getText().toString();
                String l2 = et2.getText().toString();
                String lm = et3.getText().toString();
                String city = et4.getText().toString();
                String pin = et5.getText().toString();

                if(l1.isEmpty())
                {
                    et1.setError("Field Required");
                    et1.requestFocus();
                    return;
                }
                if(l2.isEmpty())
                {
                    et2.setError("Field Required");
                    et2.requestFocus();
                    return;
                }
                if(lm.isEmpty())
                {
                    et3.setError("Field Required");
                    et3.requestFocus();
                    return;
                }
                if(city.isEmpty())
                {
                    et4.setError("Field Required");
                    et4.requestFocus();
                    return;
                }
                if(pin.isEmpty())
                {
                    et5.setError("Field Required");
                    et5.requestFocus();
                    return;
                }

                address = et1.getText().toString()+" " +et2.getText().toString()+" "+et3.getText().toString()+" "+et4.getText().toString()+" "+et5.getText().toString();
                new saveaddress().execute(sharedPreferences.getString("Id",""),address);
            }
        });
    }

    public class saveaddress extends AsyncTask<String,Void,String>{

        String u1 = "";

        @Override
        protected void onPreExecute() {
            u1 = getResources().getString(R.string.connection)+"saveAddress.php";
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String userid = strings[0];
            String address = strings[1];
            try{
                URL url=new URL(u1);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                //Log.d("Connect","Done");

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

                String data = URLEncoder.encode("userid","UTF-8")+"="+URLEncoder.encode(userid,"UTF-8");
                data+="&"+URLEncoder.encode("address","UTF-8")+"="+URLEncoder.encode(address,"UTF-8");

                bw.write(data);

                bw.flush();
                bw.close();

                try{
                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
                    //Log.d("Connect2","Done");
                    StringBuilder builder=new StringBuilder();
                    String line="";

                    while ((line=br.readLine())!=null)
                    {
                        builder.append(line);
                        //Log.d("Connect3","Done");
                    }

                    br.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return  builder.toString();
                }
                catch(Exception ex)
                {
                    //Log.d("ErrorF",ex.getMessage().toString());
                }

            }
            catch(Exception ex)
            {
                //Log.d("ErrorFinal",ex.getMessage().toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //Toast.makeText(AddAddress.this, s, Toast.LENGTH_SHORT).show();
            if(s.equals("Address Saved"))
            {
                Intent intent = new Intent(AddAddress.this,ShoppingCart.class);
                finish();
                startActivity(intent);

            }
            else{
                Toast.makeText(AddAddress.this, "Uknown Error, Please Try Again Later", Toast.LENGTH_LONG).show();
                startActivity(new Intent(AddAddress.this,ShoppingCart.class));
                finish();
            }
            super.onPostExecute(s);
        }
    }
}
