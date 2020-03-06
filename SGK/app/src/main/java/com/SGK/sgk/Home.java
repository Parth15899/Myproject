package com.SGK.sgk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    CardView cv1,cv2;

    RecyclerView gv1;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    ArrayList<String> myarr,pricelist;
    ArrayList<productClass> myList;
    Button btn1;
    EditText et1;

    ProgressBar pbhome;
    String json_data;

    JSONArray jsonArray;
    JSONObject jsonObject;
    NavigationView navigationView;

    featuredProductAdapter featuredproductadapter;
    TextView navHeadName,navHeadContact,navHeadEmail;
    SharedPreferences sharedPreferences;
    Animation animation1,animation2;
    private final String MyPrefs = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        animation1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
        animation2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);

        sharedPreferences = getSharedPreferences(MyPrefs,Context.MODE_PRIVATE);




        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.cart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this,ShoppingCart.class));
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navHeadName = (TextView)headerView.findViewById(R.id.header_name);
        navHeadContact = (TextView)headerView.findViewById(R.id.header_contact);
        navHeadEmail = (TextView)headerView.findViewById(R.id.header_email);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //cardviewIntent
        pbhome = findViewById(R.id.pbhome);
        cv1=findViewById(R.id.cvoffer);
        cv2=findViewById(R.id.cvproduct);

        gv1 = findViewById(R.id.gv1);
        gv1.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        gv1.setLayoutManager(layoutManager);

        et1 = findViewById(R.id.searchtext);
        myarr = new ArrayList<String>();
        pricelist = new ArrayList<String>();
        myList = new ArrayList<>();
        pbhome.setVisibility(View.GONE);
        new showfeatprod().execute();
        cv1.setVisibility(View.VISIBLE);
        cv2.setVisibility(View.VISIBLE);
        new fetchUser().execute(sharedPreferences.getString("Contact",""));
        cv1.startAnimation(animation1);
        cv2.startAnimation(animation1);
        et1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN)
                    if((i == KeyEvent.KEYCODE_DPAD_CENTER) || (i==KeyEvent.KEYCODE_ENTER))
                    {
                        Intent intent = new Intent(Home.this,searchAct.class);
                        intent.putExtra("searchtext",et1.getText().toString());
                        startActivity(intent);
                        return true;
                    }

                return false;
            }
        });


        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(Home.this,R.anim.click));
                startActivity(new Intent(Home.this,offers.class));
            }
        });

        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(Home.this,R.anim.click));
                startActivity(new Intent(Home.this,Product.class));
            }
        });



        //cardviewIntent

    }

    public class fetchUser extends AsyncTask<String,Void,String>{

        String u1 = "";

        @Override
        protected void onPreExecute() {
            u1 = getResources().getString(R.string.connection)+"fetchUser.php";
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String contact = strings[0];

            try{
                URL url=new URL(u1);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                //Log.d("Connect","Done");

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

                String data = URLEncoder.encode("contact","UTF-8")+"="+URLEncoder.encode(contact,"UTF-8");

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

            String val[] = s.split(",");

            if(val[1].equals("No"))
            {
                Intent intent = new Intent(Home.this,SignUp.class);
                finish();
                startActivity(intent);
            }
            else {


                //Toast.makeText(Home.this, s, Toast.LENGTH_SHORT).show();
                json_data = s;
                try {

                    jsonObject = new JSONObject(json_data);
                    jsonArray = jsonObject.getJSONArray("Server_Response");

                    int count = 0;

                    while (count < jsonArray.length()) {

                        JSONObject jb = jsonArray.getJSONObject(count);

                        String id = jb.getString("userid");
                        String name = jb.getString("name");
                        String contact = jb.getString("contact");
                        String email = jb.getString("email");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Name", name);
                        editor.putString("Id", id);
                        editor.putString("Email", email);
                        editor.putString("logged", "logged");
                        editor.commit();
                        navHeadName.setText(sharedPreferences.getString("Name", ""));
                        navHeadContact.setText(sharedPreferences.getString("Contact", ""));
                        navHeadEmail.setText(sharedPreferences.getString("Email", ""));
                        count++;
                    }

                } catch (Exception ex) {

                }
            }
            super.onPostExecute(s);
        }
    }

    public class showfeatprod extends AsyncTask<String, Void, String> {

        String u1="";
        @Override
        protected void onPreExecute() {
            pbhome.setVisibility(View.VISIBLE);
            //gv1.setVisibility(View.GONE);
            u1 = getResources().getString(R.string.connection)+"showFeaturedProduct.php";
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                URL url=new URL(u1);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                Log.d("Connect","Done");
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
            pbhome.setVisibility(View.GONE);
            gv1.setVisibility(View.VISIBLE);
            featuredproductadapter = new featuredProductAdapter(Home.this,R.layout.showfeatureproduct);
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
                    myarr.add(pid);
                    pricelist.add(pprice);
                    productClass productclass =new productClass(pidInt,pname,pprice,pimage,1);
                    featuredproductadapter.add(productclass);
                    myList.add(productclass);

                    adapter = new fpadapter(Home.this,myList);
                    gv1.setAdapter(adapter);
                    count++;
                }
                //gv1.setAdapter(featuredproductadapter);

                gv1.setAnimation(animation2);


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
            AlertDialog.Builder ab = new AlertDialog.Builder(Home.this);
            ab.setTitle("LogOut");
            ab.setIcon(R.drawable.logo);
            ab.setMessage("Are You Sure you want to Logout?");
            ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Home.this,Login.class);
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

        } else if (id == R.id.nav_category) {
            startActivity(new Intent(Home.this,Act_Category.class));

        } else if (id == R.id.nav_offers) {
            startActivity(new Intent(Home.this,offers.class));

        } else if (id == R.id.nav_products) {
            startActivity(new Intent(Home.this,Product.class));
        }
        else if (id == R.id.nav_myorders) {
            startActivity(new Intent(Home.this,MyOrders.class));
        }
        else if (id == R.id.nav_profile) {
            startActivity(new Intent(Home.this,Profile.class));
        }
        else if (id == R.id.tnc) {
            startActivity(new Intent(Home.this,Terms.class));
        }
        else if (id == R.id.nav_contact) {
            startActivity(new Intent(Home.this,ContactUs.class));
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
