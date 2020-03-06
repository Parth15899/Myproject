package com.SGK.sgk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Act_Category extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String json_data;
    JSONArray jsonArray;
    JSONObject jsonObject;
    GridView gv1;
    ArrayList<String> list1;
    CategoryAdapter categoryAdapter;
    SharedPreferences sharedPreferences;
    private final String MyPrefs = "MyPrefs";

    ProgressBar pb;
    TextView navHeadName,navHeadContact,navHeadEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__category);
        sharedPreferences = getSharedPreferences(MyPrefs,Context.MODE_PRIVATE);

        pb = findViewById(R.id.pbcat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.cart);

        pb.setVisibility(View.GONE);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        navHeadName = (TextView)headerView.findViewById(R.id.header_name);
        navHeadContact = (TextView)headerView.findViewById(R.id.header_contact);
        navHeadEmail = (TextView)headerView.findViewById(R.id.header_email);


        navHeadName.setText(sharedPreferences.getString("Name",""));
        navHeadContact.setText(sharedPreferences.getString("Contact",""));
        navHeadEmail.setText(sharedPreferences.getString("Email",""));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        gv1 = findViewById(R.id.gvCategory);
        list1 = new ArrayList<String>();

        new showcategory().execute();
    }

    public class showcategory extends AsyncTask<String, Void, String>{

        String u1 = "";

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
            u1 = getResources().getString(R.string.connection)+"fetchcategory.php";
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                URL url=new URL(u1);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
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

            categoryAdapter = new CategoryAdapter(Act_Category.this,R.layout.showcategory);
            json_data=s;
            try{

                jsonObject = new JSONObject(json_data);
                jsonArray = jsonObject.getJSONArray("Server_Response");

                int count=0;

                while(count<jsonArray.length())
                {
                    JSONObject jb = jsonArray.getJSONObject(count);

                    String catname = jb.getString("catname");

                    String catpic = jb.getString("catpic");
                    String catid = jb.getString("catid");
                    int catIdInt = Integer.parseInt(catid);
                    list1.add(catid);
                    CategoryClass categoryClass =new CategoryClass(catIdInt,catname,catpic);
                    categoryAdapter.add(categoryClass);

                    count++;
                }
                gv1.setAdapter(categoryAdapter);
                pb.setVisibility(View.GONE);

                gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String catid = list1.get(i);

                        Intent intent = new Intent(Act_Category.this,SubCategory.class);
                        intent.putExtra("catid",catid);
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            AlertDialog.Builder ab = new AlertDialog.Builder(Act_Category.this);
            ab.setTitle("LogOut");
            ab.setIcon(R.drawable.logsgk);
            ab.setMessage("Are You Sure you want to Logout?");
            ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Act_Category.this,Login.class);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog ad = ab.create();
            ad.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(Act_Category.this,Home.class));
        } else if (id == R.id.nav_category) {
            //startActivity(new Intent(Act_Category.this,Act_Category.class));

        } else if (id == R.id.nav_offers) {
            startActivity(new Intent(Act_Category.this,offers.class));

        } else if (id == R.id.nav_products) {
            startActivity(new Intent(Act_Category.this,Product.class));
        }
        else if (id == R.id.nav_myorders) {
            startActivity(new Intent(Act_Category.this,MyOrders.class));
        }
        else if (id == R.id.nav_profile) {
            startActivity(new Intent(Act_Category.this,Profile.class));
        }
        else if (id == R.id.tnc) {
            startActivity(new Intent(Act_Category.this,Terms.class));
        }
        else if (id == R.id.nav_contact) {
            startActivity(new Intent(Act_Category.this,ContactUs.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
