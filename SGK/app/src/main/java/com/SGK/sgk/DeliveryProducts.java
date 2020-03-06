package com.SGK.sgk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

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

public class DeliveryProducts extends AppCompatActivity {

    ListView lv1;
    Button btn1;
    ArrayList<String> list1,list2;
    String userid;
    String json_data;
    JSONArray jsonArray;
    JSONObject jsonObject;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_products);
        lv1 = findViewById(R.id.lvproddel);
        btn1 = findViewById(R.id.btnDeliver);
        list1 = new ArrayList<String>();
        list2 = new ArrayList<String>();
        userid = getIntent().getStringExtra("userid");

        new fetchDeliveries().execute(userid);
    }

    public class fetchDeliveries extends AsyncTask<String,Void,String> {

        String u1="";

        @Override
        protected void onPreExecute() {
            u1=getResources().getString(R.string.connection)+"deliverProduct.php";
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            String userid = strings[0];
            try{
                URL url= new URL(u1);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(outputStream));

                String data = URLEncoder.encode("userid","UTF-8")+"="+URLEncoder.encode(userid,"UTF-8");

                bw.write(data);
                bw.flush();
                bw.close();

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
            Toast.makeText(DeliveryProducts.this, s, Toast.LENGTH_SHORT).show();
            json_data=s;
            try {

                jsonObject = new JSONObject(json_data);
                jsonArray = jsonObject.getJSONArray("Server_Response");

                int count = 0;

                while (count < jsonArray.length()) {
                    JSONObject jb = jsonArray.getJSONObject(count);

                    list1.add(jb.getString("pname"));
                    list2.add(jb.getString("orderid"));
                    email = jb.getString("email");
                    count++;
                }
                ArrayAdapter adapter = new ArrayAdapter(DeliveryProducts.this,android.R.layout.simple_list_item_1,list1);
                lv1.setAdapter(adapter);

                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for(int i=0;i<list2.size();i++)
                        {
                            new updateData().execute(list2.get(i));
                        }

                        String pnames = "";
                        for(int j=0;j<list1.size();j++)
                        {
                            pnames = pnames +list1.get(j)+"\n";
                        }

                        String[]to={email};

                        String subject="Your Order from SGK";

                        String body="Dear Customer,\n\n Your Order with Products : \n"+pnames+"\n Has been delivered to You On time \n\n Happy To Serve You.";

                        Intent intent=new Intent(Intent.ACTION_SEND);
                        intent.setData(Uri.parse("mail to:"));
                        intent.setType("text/plain");

                        intent.putExtra(Intent.EXTRA_EMAIL,to);
                        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
                        intent.putExtra(Intent.EXTRA_TEXT,body);

                        Intent chooser=Intent.createChooser(intent,"Send To");

                        startActivity(intent);
                        finish();
                    }
                });

            }
            catch(Exception ex)
            {

            }


            super.onPostExecute(s);
        }
    }


    public class updateData extends AsyncTask<String,Void,String> {

        String u2="";

        @Override
        protected void onPreExecute() {
            u2=getResources().getString(R.string.connection)+"updateDelivery.php";
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            String orderid = strings[0];
            try{
                URL url= new URL(u2);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(outputStream));

                String data = URLEncoder.encode("orderid","UTF-8")+"="+URLEncoder.encode(orderid,"UTF-8");

                bw.write(data);
                bw.flush();
                bw.close();

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
            Toast.makeText(DeliveryProducts.this, s, Toast.LENGTH_LONG).show();
            super.onPostExecute(s);
        }
    }


}
