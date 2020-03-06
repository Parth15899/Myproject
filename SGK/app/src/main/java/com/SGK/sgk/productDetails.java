package com.SGK.sgk;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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

public class productDetails extends AppCompatActivity {

    addtocart obj;
    String pid;
    TextView tvproduct,tvprice,tvdesc,tvquant;
    RatingBar rb1;
    Button btnAdd,btnplus,btnminus;
    ImageView iv1;
    String json_data;
    JSONArray jsonArray;
    JSONObject jsonObject;
    String qprice;
    String prid;
    LinearLayout ln1,llp,lldesc;
    SharedPreferences sharedPreferences;
    private final String MyPrefs = "MyPrefs";

    int changequant;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        qprice = getIntent().getStringExtra("pprice");
        setContentView(R.layout.activity_product_details);
        tvproduct = findViewById(R.id.prodpname);
        tvprice = findViewById(R.id.detailpprice);
        tvdesc = findViewById(R.id.proddesc);
        tvquant = findViewById(R.id.quant);
        ln1 = findViewById(R.id.llpbproddet);
        llp = findViewById(R.id.llp);
        lldesc = findViewById(R.id.lldesc);
        btnAdd = findViewById(R.id.btnaddtocart);
        btnminus = findViewById(R.id.prodquantminus);
        btnplus = findViewById(R.id.prodquantplus);
        iv1 = findViewById(R.id.detailImage);
        iv1.setClipToOutline(true);
        pid = getIntent().getStringExtra("pid");

        obj = new addtocart(productDetails.this);

        new detailproduct().execute(pid);





        //tv1.setText(pid);
    }

    public class detailproduct extends AsyncTask<String, Void, String>{

        String u1 = "";
        @Override
        protected void onPreExecute() {
            ln1.setVisibility(View.VISIBLE);
            llp.setAlpha((float) 0.5);
            lldesc.setVisibility(View.GONE);
            u1 = getResources().getString(R.string.connection)+"productDetail.php";
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                String pid = strings[0];
                URL url=new URL(u1);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                //Log.d("Connect","Done");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

                String data = URLEncoder.encode("pid","UTF-8")+"="+URLEncoder.encode(pid,"UTF-8");

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

            json_data=s;
            llp.setAlpha((float) 1.0);
            lldesc.setVisibility(View.VISIBLE);
            try{

                jsonObject = new JSONObject(json_data);
                jsonArray = jsonObject.getJSONArray("Server_Response");

                int count=0;

                while(count<jsonArray.length())
                {
                    JSONObject jb = jsonArray.getJSONObject(count);

                    String pname = jb.getString("pname");

                    String pprice=jb.getString("pprice");
                    //String qprice = jb.getString("pprice");
                    String pimage =getResources().getString(R.string.imgprod) + jb.getString("pimage");
                    prid = jb.getString("pid");
                    int pidInt = Integer.parseInt(prid);
                    String pdesc = jb.getString("pdesc");

                    tvproduct.setText(pname);
                    tvprice.setText(qprice);
                    //rb1.setRating((float)4);
                    tvdesc.setText(pdesc);

                    Glide.with(productDetails.this)
                            .load(pimage)
                            .placeholder(R.drawable.logo)

                            .into(iv1);

                    count++;
                }

                ln1.setVisibility(View.GONE);
                btnplus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String changequantst= tvquant.getText().toString();
                        if(changequant < 10)
                        {
                            changequant = changequant+1;

                            String finalq = String.valueOf(changequant);

                            tvquant.setText(finalq);
                        }
                        else{
                            Toast.makeText(productDetails.this, "Quantity Cannot be More than 10", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                btnminus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String changequantst= tvquant.getText().toString();
                        changequant = Integer.parseInt(changequantst);

                        if(changequant > 1)
                        {
                            changequant = changequant-1;

                            String finalq = String.valueOf(changequant);

                            tvquant.setText(finalq);
                        }
                        else{
                            Toast.makeText(productDetails.this, "Quantity Cannot be less than 1", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int addprice = Integer.parseInt(tvprice.getText().toString());
                        int addquant = Integer.parseInt(tvquant.getText().toString());
                        int totprice = addprice * addquant;
                        new addtocart(productDetails.this).execute(sharedPreferences.getString("Id",""),prid,tvquant.getText().toString(),tvprice.getText().toString(),String.valueOf(totprice));
                        //Toast.makeText(productDetails.this, prid, Toast.LENGTH_SHORT).show();
                        //obj.execute(sharedPreferences.getString("Id",""),prid,tvquant.getText().toString(),tvprice.getText().toString(),String.valueOf(totprice));
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
