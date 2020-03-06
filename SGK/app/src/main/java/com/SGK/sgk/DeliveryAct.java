package com.SGK.sgk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DeliveryAct extends AppCompatActivity {
ListView lv;
ArrayList<String> list1,list2;
    String json_data;
    JSONArray jsonArray;
    JSONObject jsonObject;
    FloatingActionButton fab;
    SharedPreferences sharedPreferences;
    private static String Myprefs = "MyPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        lv=findViewById(R.id.lvaddress);
        list1=new ArrayList<String>();
        fab = findViewById(R.id.logout);
        list2=new ArrayList<String>();
        sharedPreferences = getSharedPreferences(Myprefs, Context.MODE_PRIVATE);

        new fetchaddress().execute();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(DeliveryAct.this,SignUp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }


    public class fetchaddress extends AsyncTask<String,Void,String>{

        String u1="";

        @Override
        protected void onPreExecute() {
            u1=getResources().getString(R.string.connection)+"fetchDeliveryAdd.php";
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                URL url= new URL(u1);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                //OutputStream outputStream=httpURLConnection.getOutputStream();
                //BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(outputStream));

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder builder = new StringBuilder();
                String line = "";

                while ((line = br.readLine()) != null) {
                    builder.append(line);
                    //Log.d("Connect3","Done");
                }

                br.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return builder.toString();
            }
            catch(Exception ex)
            {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //Toast.makeText(DeliveryAct.this, s, Toast.LENGTH_SHORT).show();
            json_data=s;
            try {

                jsonObject = new JSONObject(json_data);
                jsonArray = jsonObject.getJSONArray("Server_Response");

                int count = 0;

                while (count < jsonArray.length()) {
                    JSONObject jb = jsonArray.getJSONObject(count);

                    list1.add(jb.getString("address"));
                    list2.add(jb.getString("userid"));

                    count++;
                }
                ArrayAdapter adapter = new ArrayAdapter(DeliveryAct.this,android.R.layout.simple_list_item_1,list1);
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String userid = list2.get(i);
                        Intent intent = new Intent(DeliveryAct.this,DeliveryProducts.class);
                        intent.putExtra("userid",userid);
                        startActivity(intent);
                    }
                });
            }
            catch(Exception ex)
            {

            }


            super.onPostExecute(s);
        }
    }
}
