package com.SGK.sgk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class ShoppingCart extends AppCompatActivity {

    String json_data;
    JSONArray jsonArray;
    JSONObject jsonObject;
    Button btn1,btn2;
    GridView lv1;
    LinearLayout lvpb;
    TextView tvAddress,tvempty;
    ImageView ivempty;
    ProgressBar pbcart;
    CartAdapter cartAdapter;
    TextView tv1;
    ArrayList<Integer> pidlist,quantlist,pricelist,totallist;
    String address;
    int grandtotal;
    sendorder sord;
    SharedPreferences sharedPreferences;
    private final String MyPrefs = "MyPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        sharedPreferences = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);


        pidlist= new ArrayList<>();
        quantlist = new ArrayList<>();
        pricelist = new ArrayList<>();
        totallist = new ArrayList<>();
        tv1 = findViewById(R.id.delto);
        tvempty = findViewById(R.id.txtempty);
        ivempty = findViewById(R.id.imgempty);
        tvAddress = findViewById(R.id.tvAddress);
        pidlist.clear();
        quantlist.clear();
        pricelist.clear();
        totallist.clear();

        lv1 = findViewById(R.id.lvcart);
        lvpb = findViewById(R.id.lvpb);
        btn1 = findViewById(R.id.btnproceed);
        btn2 = findViewById(R.id.btnAddress);
        btn2.setVisibility(View.GONE);
        btn1.setVisibility(View.GONE);
        tvAddress.setVisibility(View.GONE);
        lvpb.setVisibility(View.GONE);
        tv1.setVisibility(View.GONE);
        //tv1.setText("Bag seems to be extra light. Add Product to make it look Good and Heavy.");
        new fetchcart().execute(sharedPreferences.getString("Id",""));
    }

    public class fetchcart extends AsyncTask<String, Void, String>{

        String u1 = "";
        @Override
        protected void onPreExecute() {
            lvpb.setVisibility(View.VISIBLE);
            u1 = getResources().getString(R.string.connection)+"fetchcart.php";
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
                //Log.d("Connect","Done");

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

            lvpb.setVisibility(View.GONE);
            lv1.setVisibility(View.VISIBLE);
            cartAdapter = new CartAdapter(ShoppingCart.this,R.layout.cartlist);
            json_data=s;
            try{

                jsonObject = new JSONObject(json_data);
                jsonArray = jsonObject.getJSONArray("Server_Response");

                int count=0;

                while(count<jsonArray.length())
                {
                    JSONObject jb = jsonArray.getJSONObject(count);

                    int pid = jb.getInt("pid");
                    String pname = jb.getString("pname");
                    String pimage = jb.getString("pimage");
                    int quant = jb.getInt("quant");
                    int price = jb.getInt("price");
                    int total = jb.getInt("total");
                    address = jb.getString("address");

                    pidlist.add(pid);
                    quantlist.add(quant);
                    pricelist.add(price);
                    totallist.add(total);

                    CartClass cartClass = new CartClass(1,pid,quant,price,total,pname,pimage);
                    cartAdapter.add(cartClass);

                    count++;
                }
                if(count>0)
                {
                    ivempty.setVisibility(View.GONE);
                    tvempty.setVisibility(View.GONE);
                }

                lv1.setAdapter(cartAdapter);

                for(int i=0;i<totallist.size();i++)
                {
                    grandtotal = grandtotal + totallist.get(i);
                }

                if(address.equals("Null"))
                {
                    tv1.setText("Delivering to : ");tv1.setVisibility(View.VISIBLE);
                    tvAddress.setVisibility(View.VISIBLE);
                    btn1.setVisibility(View.GONE);
                    btn2.setVisibility(View.VISIBLE);
                    btn1.setClickable(false);
                    btn2.setText("Add Address");
                    tvAddress.setText("Please Provide Address to continue Ordering..");
                    btn2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ShoppingCart.this,AddAddress.class);
                            startActivity(intent);
                        }
                    });
                }
                else{
                    tv1.setText("Delivering to : ");
                    tvAddress.setVisibility(View.VISIBLE);
                    btn1.setVisibility(View.VISIBLE);
                    btn2.setVisibility(View.VISIBLE);
                    btn1.setText("Place Order : "+getResources().getString(R.string.rupee)+grandtotal);
                    btn2.setText("Change Address");
                    tvAddress.setText(address);
                    btn2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ShoppingCart.this,AddAddress.class);
                            startActivity(intent);
                        }
                    });
                    btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AlertDialog.Builder ab = new AlertDialog.Builder(ShoppingCart.this);
                            ab.setTitle("Confirm Order");
                            ab.setIcon(R.drawable.logsgk);

                            ab.setMessage("Confirm to proceed with your Order?");
                            ab.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    sord = new sendorder(ShoppingCart.this);

                                    for(int j=0;j<pidlist.size();j++)
                                    {
                                        //int insuserid = 1;
                                        int inspid = pidlist.get(j);
                                        int insquant = quantlist.get(j);
                                        int insprice = pricelist.get(j);
                                        int instotalprice = totallist.get(j);
                                        int insgrandtotal = grandtotal;
                                        new sendorder(ShoppingCart.this).execute(sharedPreferences.getString("Id",""),String.valueOf(inspid),String.valueOf(insquant),String.valueOf(insprice),String.valueOf(instotalprice),String.valueOf(insgrandtotal));
                                        //sord.execute();
                                    }

                                    //sord.execute();
                                }
                            });
                            ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            AlertDialog alertDialog = ab.create();
                            alertDialog.show();
                        }
                    });
                }
            }
            catch(Exception ex)
            {

            }
            super.onPostExecute(s);
        }
    }
}
