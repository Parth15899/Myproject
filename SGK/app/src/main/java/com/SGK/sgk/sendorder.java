package com.SGK.sgk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class sendorder extends AsyncTask<String,Void, String> {

    String u1 = "";
    Context ctx;
    public sendorder(Context context){
        this.ctx = context;
    }

    @Override
    protected void onPreExecute() {
        u1 = ctx.getResources().getString(R.string.connection)+"sendorder.php";
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String userid = strings[0];
        String pid = strings[1];
        String quant = strings[2];
        String price = strings[3];
        String totprice = strings[4];
        String grandtotal = strings[5];

        try{
            URL url=new URL(u1);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            //Log.d("Connect","Done");

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

            String data = URLEncoder.encode("userid","UTF-8")+"="+URLEncoder.encode(userid,"UTF-8");
            data+="&"+URLEncoder.encode("pid","UTF-8")+"="+URLEncoder.encode(pid,"UTF-8");
            data+="&"+URLEncoder.encode("quant","UTF-8")+"="+URLEncoder.encode(quant,"UTF-8");
            data+="&"+URLEncoder.encode("price","UTF-8")+"="+URLEncoder.encode(price,"UTF-8");
            data+="&"+URLEncoder.encode("totprice","UTF-8")+"="+URLEncoder.encode(totprice,"UTF-8");
            data+="&"+URLEncoder.encode("grandtotal","UTF-8")+"="+URLEncoder.encode(grandtotal,"UTF-8");

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

        if(s.equals("Success"))
        {
            Intent intent = new Intent(ctx,ConfirmOrder.class);
            ((Activity)ctx).finish();
            ctx.startActivity(intent);

        }
        else{
            Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
        }
        super.onPostExecute(s);
    }
}
