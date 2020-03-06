package com.SGK.sgk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

public class ProductCat extends AppCompatActivity {

    String json_data;
    JSONArray jsonArray;
    JSONObject jsonObject;

    ArrayList<String> list1,list2;
    productAdapter productadapter;
    ArrayList<productClass> myList;

    String catid,subcatid;
    LinearLayout ln1;
    RecyclerView gv1;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_cat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductCat.this,ShoppingCart.class));
            }
        });
        gv1 = findViewById(R.id.gvprod);
        layoutManager = new GridLayoutManager(ProductCat.this,2);
        gv1.setLayoutManager(layoutManager);
        gv1.setHasFixedSize(true);
        list1 = new ArrayList<String>();
        list2 = new ArrayList<String>();
        myList = new ArrayList<productClass>();
        ln1 = findViewById(R.id.llpbprod);
        catid = getIntent().getStringExtra("catid");
        subcatid = getIntent().getStringExtra("subcatid");
        new fetchcatproduct().execute(catid,subcatid);

    }

    public class fetchcatproduct extends AsyncTask<String, Void, String> {

        String u1 = "";
        @Override
        protected void onPreExecute() {
            ln1.setVisibility(View.VISIBLE);
            u1 = getResources().getString(R.string.connection)+"fetchprodcat.php";
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String catid = strings[0];
            String subcatid = strings[1];
            try{
                URL url=new URL(u1);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

                String data = URLEncoder.encode("catid","UTF-8")+"="+URLEncoder.encode(catid,"UTF-8");
                data+="&"+URLEncoder.encode("subcatid","UTF-8")+"="+URLEncoder.encode(subcatid,"UTF-8");

                bw.write(data);

                bw.flush();
                bw.close();
                //Log.d("Connect","Done");
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
            productadapter = new productAdapter(ProductCat.this,R.layout.showproduct);
            json_data=s;
            try{

                jsonObject = new JSONObject(json_data);
                jsonArray = jsonObject.getJSONArray("Server_Response");

                int count=0;

                while(count<jsonArray.length())
                {
                    JSONObject jb = jsonArray.getJSONObject(count);

                    String pname = jb.getString("pname");
                    //String pprice = jb.getString("pprice");
                    String pprice=jb.getString("pprice");
                    //byte[] pimage = (byte[]) jb.get("pimage");
                    //String pimage = null;
                    String pimage = jb.getString("pimage");
                    String pid = jb.getString("pid");
                    int pidInt = Integer.parseInt(pid);
                    list1.add(pid);
                    list2.add(String.valueOf(pprice));
                    productClass productclass =new productClass(pidInt,pname,pprice,pimage,1);
                    productadapter.add(productclass);
                    myList.add(productclass);
                    adapter = new padapter(ProductCat.this,myList);
                    gv1.setAdapter(adapter);

                    count++;
                }

                ln1.setVisibility(View.GONE);


            }
            catch(Exception ex)
            {

            }
            super.onPostExecute(s);
        }
    }
}
