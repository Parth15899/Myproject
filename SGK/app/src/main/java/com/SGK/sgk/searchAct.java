package com.SGK.sgk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

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

public class searchAct extends AppCompatActivity {

    RecyclerView gvsearch;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    String search;
    String json_data;
    JSONArray jsonArray;
    JSONObject jsonObject;
    ArrayList<String> list1,list2;
    productAdapter productadapter;
    Button btn1;
    LinearLayout ll1,ll2;
    EditText et1;
    Animation animation;
    ArrayList<productClass> myList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        gvsearch = findViewById(R.id.gvsearch);
        layoutManager = new GridLayoutManager(searchAct.this,2);
        gvsearch.setHasFixedSize(true);
        gvsearch.setLayoutManager(layoutManager);

        animation = AnimationUtils.loadAnimation(searchAct.this,R.anim.fade);

        et1 = findViewById(R.id.searchtext);

        ll1 = findViewById(R.id.llpbprod);
        ll2 = findViewById(R.id.llgrid);
        search = getIntent().getStringExtra("searchtext");
        et1.setText(search);
        list1 = new ArrayList<String>();
        list2 = new ArrayList<String>();
        myList = new ArrayList<productClass>();
        new searchProd().execute(search);

        et1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN)
                    if((i == KeyEvent.KEYCODE_DPAD_CENTER) || (i==KeyEvent.KEYCODE_ENTER))
                    {
                        list1.clear();
                        list2.clear();
                        myList.clear();
                        String st = et1.getText().toString();
                        new searchProd().execute(st);
                        return true;
                    }

                return false;
            }
        });


    }

    public class searchProd extends AsyncTask<String,Void,String>{

        String u1 = "";
        @Override
        protected void onPreExecute() {
            ll1.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.GONE);
            u1 = getResources().getString(R.string.connection)+"searchProduct.php";
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String pname = strings[0];
            try{
                URL url=new URL(u1);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                //Log.d("Connect","Done");

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

                String data = URLEncoder.encode("pname","UTF-8")+"="+URLEncoder.encode(pname,"UTF-8");

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
            ll1.setVisibility(View.GONE);
            ll2.setVisibility(View.VISIBLE);

            //Toast.makeText(searchAct.this, s, Toast.LENGTH_SHORT).show();
            productadapter = new productAdapter(searchAct.this,R.layout.showproduct);
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
                    list2.add(pprice);
                    productClass productclass =new productClass(pidInt,pname,pprice,pimage,1);
                    productadapter.add(productclass);
                    myList.add(productclass);
                    adapter = new padapter(searchAct.this,myList);
                    gvsearch.setAdapter(adapter);
                    gvsearch.setAnimation(animation);
                    count++;
                }




            }
            catch(Exception ex)
            {

            }

            super.onPostExecute(s);
        }
    }
}
