package com.SGK.sgk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;

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


public class SubCategory extends AppCompatActivity {

    GridView gv1;
    String json_data;
    JSONArray jsonArray;
    JSONObject jsonObject;
    ListView lv1;
    String catid;

    ProgressBar pb;
    ArrayList<String> list1,list2;
    SubCategoryAdapter subCategoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);



        pb = findViewById(R.id.pbsubcat);
        gv1 = findViewById(R.id.gvsubcateg);
        list1 = new ArrayList<String>();
        list2 = new ArrayList<String>();
        pb.setVisibility(View.GONE);
        catid = getIntent().getStringExtra("catid");
        new fetchsubcateg().execute(catid);

    }

    public class fetchsubcateg extends AsyncTask<String, Void, String>{

        String u1 = "";
        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
            u1 = getResources().getString(R.string.connection)+"fetchSubcateg.php";
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String catid = strings[0];
            try{
                URL url=new URL(u1);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                //Log.d("Connect","Done");

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

                String data = URLEncoder.encode("catid","UTF-8")+"="+URLEncoder.encode(catid,"UTF-8");

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
            catch(Exception ex){

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            subCategoryAdapter = new SubCategoryAdapter(SubCategory.this,R.layout.showcategory);
            json_data=s;
            try{

                jsonObject = new JSONObject(json_data);
                jsonArray = jsonObject.getJSONArray("Server_Response");

                int count=0;

                while(count<jsonArray.length())
                {
                    JSONObject jb = jsonArray.getJSONObject(count);

                    String subcatname = jb.getString("subcatname");

                    String subcatpic = jb.getString("subcatpic");
                    String catid = jb.getString("catid");
                    int catIdInt = Integer.parseInt(catid);
                    String subcatid = jb.getString("subcatid");
                    int subcatIdInt = Integer.parseInt(subcatid);
                    list1.add(catid);
                    list2.add(subcatid);
                    SubCategoryClass subCategoryClass =new SubCategoryClass(subcatIdInt,catIdInt,subcatname,subcatpic);
                    subCategoryAdapter.add(subCategoryClass);

                    count++;
                }
                gv1.setAdapter(subCategoryAdapter);
                pb.setVisibility(View.GONE);

                gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String catid = list1.get(i);
                        String subcatid = list2.get(i);
                        Intent intent = new Intent(SubCategory.this,ProductCat.class);
                        intent.putExtra("catid",catid);
                        intent.putExtra("subcatid",subcatid);
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
