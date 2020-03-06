package com.SGK.sgk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

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

public class UndispatchOrder extends AppCompatActivity {

    ListView lv1;

    String json_data;
    JSONArray jsonArray;
    JSONObject jsonObject;

    UndisOrderAdapter undisOrderAdapter;
    SharedPreferences sharedPreferences;
    private final String MyPrefs = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_undispatch_order);
        sharedPreferences = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);



        lv1 = findViewById(R.id.lvundisorder);

        new fetchundisorder().execute(sharedPreferences.getString("Id",""));

    }

    public class fetchundisorder extends AsyncTask<String,Void,String> {

        String u1 = "";

        @Override
        protected void onPreExecute() {
            u1 = getResources().getString(R.string.connection)+"fetchundisorders.php";
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String userid = strings[0];
            try{
                URL url=new URL(u1);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
               // Log.d("Connect","Done");

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

                String data = URLEncoder.encode("userid","UTF-8")+"="+URLEncoder.encode(userid,"UTF-8");

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
                        builder.append(line+"\n");
                        //Log.d("Connect3","Done");
                    }

                    br.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return  builder.toString();
                }
                catch(Exception ex)
                {
                    //Log.d("Error",ex.getMessage().toString());
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
            undisOrderAdapter = new UndisOrderAdapter(UndispatchOrder.this,R.layout.undisorder);
            json_data=s;
            try{

                jsonObject = new JSONObject(json_data);
                jsonArray = jsonObject.getJSONArray("Server_Response");

                int count=0;

                while(count<jsonArray.length())
                {
                    JSONObject jb = jsonArray.getJSONObject(count);

                    int orderid=jb.getInt("orderid");
                    int pid = jb.getInt("pid");
                    String pname = jb.getString("pname");
                    String pimage = jb.getString("pimage");
                    int quant = jb.getInt("quant");
                    int price = jb.getInt("price");
                    int total = jb.getInt("total");



                    UndisOrderClass undisOrderClass =new UndisOrderClass(pname,pimage,orderid,1,pid,quant,price,total);

                    undisOrderAdapter.add(undisOrderClass);

                    count++;
                }

                lv1.setAdapter(undisOrderAdapter);

            }
            catch(Exception ex)
            {

            }
            super.onPostExecute(s);
        }
    }
}
