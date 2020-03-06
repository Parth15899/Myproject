package com.SGK.sgk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Login extends AppCompatActivity {

    TextView tv;
    Button btn1;
    EditText et1,et2;
    ProgressBar mypb;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        btn1 = findViewById(R.id.buttonlogin);
        mypb = findViewById(R.id.progressBarLogin);
        et1 = findViewById(R.id.logincontact);
        et2 = findViewById(R.id.loginpass);
        tv = findViewById(R.id.forgotpass);
        mypb.setVisibility(View.GONE);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,fpass.class));
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mypb.setVisibility(View.VISIBLE);
                String Contact = et1.getText().toString();
                String password = et2.getText().toString();

                if(Contact.length() < 10 || Contact.isEmpty())
                {
                    et1.setError("Enter Valid Contact");
                    et1.requestFocus();
                    mypb.setVisibility(View.GONE);
                    return;
                }
                if(password.isEmpty() || password.length()<8)
                {
                    mypb.setVisibility(View.GONE);
                    et2.setError("Please Enter Valid Password");
                    et2.requestFocus();
                    return;
                }

                

                new loginuser().execute(Contact,password);
                //startActivity(new Intent(Login.this,homepage.class));
            }
        });
    }

    public class loginuser extends AsyncTask<String, Void, String>{

        String u1 = "";
        @Override
        protected void onPreExecute() {
            u1 = getResources().getString(R.string.connection)+"loginUser.php";
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String contact = strings[0];
            String password = strings[1];


            try{
                URL url = new URL(u1);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                //httpURLConnection.setDoInput(true);
                //Log.d("Buffer3","Hello3");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

                String data = URLEncoder.encode("contact","UTF-8")+"="+URLEncoder.encode(contact,"UTF-8");
                data+="&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

                //Log.d("Buffer2","Hello1");

                bw.write(data);
                bw.flush();
                bw.close();
                //Log.d("Buffer14","Hello55");

                try
                {   InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    //Log.d("Buffers","Hellos");

                    StringBuilder  builder = new StringBuilder();

                    String line = "";
                    //Log.d("Buffers","Hellos");

                    while((line = br.readLine())!=null)
                    {
                        builder.append(line);
                    }
                    br.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return builder.toString();

                }
                catch (Exception ex)
                {
                    //Log.d("Error1",ex.getMessage());
                }

            }
            catch(Exception ex)
            {
                //Log.d("Error",ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            String val[] = s.split(",");
            //Toast.makeText(Login.this, "Hello", Toast.LENGTH_SHORT).show();
            if(val[1].equals("Login Success"))
            {
                String contact = et1.getText().toString().trim();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Contact",contact);
                editor.commit();
                mypb.setVisibility(View.GONE);
                Intent intent = new Intent(Login.this,Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //intent.putExtra("contact",contact);
                startActivity(intent);
            }
            else if(val[1].equals("Login Delivery")){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Delivery","Delivery");
                editor.commit();
                mypb.setVisibility(View.GONE);
                String contact = et1.getText().toString();
                Intent intent = new Intent(Login.this,DeliveryAct.class);
                intent.putExtra("contact",contact);
                startActivity(intent);
            }
            else{
                mypb.setVisibility(View.GONE);
                Toast.makeText(Login.this, "Invalid Contact or Password", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(s);
        }
    }
}
