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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class offers extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    OfferProductAdapter offerProductAdapter;
    RecyclerView gv1;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<OfferProductClass> myList;
    String json_data;
    JSONArray jsonArray;
    Animation animation;
    JSONObject jsonObject;
    ArrayList<String> list1;
    ArrayList<String> list2;
    LinearLayout ll;
    TextView navHeadName,navHeadContact,navHeadEmail;
    SharedPreferences sharedPreferences;
    private final String MyPrefs = "MyPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        sharedPreferences = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        list1 = new ArrayList<String>();
        list2 = new ArrayList<String>();
        myList = new ArrayList<OfferProductClass>();
        ll = findViewById(R.id.prob);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(offers.this,ShoppingCart.class));
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        animation = AnimationUtils.loadAnimation(offers.this,R.anim.fade);

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
        gv1 = findViewById(R.id.gvoffer);
        layoutManager = new GridLayoutManager(offers.this,2);
        gv1.setHasFixedSize(true);
        gv1.setLayoutManager(layoutManager);


        new fetchofferproduct().execute();
    }

    public class fetchofferproduct extends AsyncTask<String, Void, String> {

        String u1 = "";
        @Override
        protected void onPreExecute() {
            ll.setVisibility(View.VISIBLE);
            gv1.setVisibility(View.GONE);
            u1 = getResources().getString(R.string.connection)+"fetchoffer.php";
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
            ll.setVisibility(View.GONE);
            gv1.setVisibility(View.VISIBLE);
            offerProductAdapter = new OfferProductAdapter(offers.this,R.layout.showofferproduct);
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
                    float offerp = Float.parseFloat(pprice);
                    String offer = jb.getString("offer");
                    float offeravail = Float.parseFloat(offer);
                    float offerprice = offerp-((offeravail/100)*offerp);
                    int offerpint = (int)offerprice;
                    //byte[] pimage = (byte[]) jb.get("pimage");
                    //String pimage = null;
                    String pimage = jb.getString("pimage");
                    String pid = jb.getString("pid");
                    int pidInt = Integer.parseInt(pid);

                    OfferProductClass offerproductclass =new OfferProductClass(pname,pimage,Integer.parseInt(pprice),(int)offerprice,pidInt);
                    //offerProductAdapter.add(offerproductclass);
                    myList.add(offerproductclass);
                    adapter = new opadapter(offers.this,myList);
                    gv1.setAdapter(adapter);

                    count++;
                }
                gv1.setAnimation(animation);



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
            AlertDialog.Builder ab = new AlertDialog.Builder(offers.this);
            ab.setTitle("LogOut");
            ab.setIcon(R.drawable.logsgk);
            ab.setMessage("Are You Sure you want to Logout?");
            ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(offers.this,Login.class);
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
            startActivity(new Intent(offers.this,Home.class));
        } else if (id == R.id.nav_category) {
            startActivity(new Intent(offers.this,Act_Category.class));

        } else if (id == R.id.nav_offers) {
            startActivity(new Intent(offers.this,offers.class));

        } else if (id == R.id.nav_products) {
            startActivity(new Intent(offers.this,Product.class));
        }
        else if (id == R.id.nav_myorders) {
            startActivity(new Intent(offers.this,MyOrders.class));
        }
        else if (id == R.id.nav_profile) {
            startActivity(new Intent(offers.this,Profile.class));
        }
        else if (id == R.id.tnc) {
            startActivity(new Intent(offers.this,Terms.class));
        }
        else if (id == R.id.nav_contact) {
            startActivity(new Intent(offers.this,ContactUs.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
