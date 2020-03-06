package com.SGK.sgk;

import android.content.Context;
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

public class addtocart extends AsyncTask<String, Void, String> {

    String u1 = "";
    Context ctx;
    public addtocart(Context context)
    {
        this.ctx = context;
    }

    @Override
    protected void onPreExecute() {
        u1 = ctx.getResources().getString(R.string.connection)+"addtoCart.php";
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String userid = strings[0];
        String pid = strings[1];
        String quant = strings[2];
        String price = strings[3];
        String total = strings[4];

        try{
            URL url = new URL(u1);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            String data = URLEncoder.encode("user","UTF-8")+"="+URLEncoder.encode(userid,"UTF-8");
            data+="&"+URLEncoder.encode("pid","UTF-8")+"="+URLEncoder.encode(pid,"UTF-8");
            data+="&"+URLEncoder.encode("quant","UTF-8")+"="+URLEncoder.encode(quant,"UTF-8");
            data+="&"+URLEncoder.encode("price","UTF-8")+"="+URLEncoder.encode(price,"UTF-8");
            data+="&"+URLEncoder.encode("total","UTF-8")+"="+URLEncoder.encode(total,"UTF-8");

            bw.write(data);
            bw.flush();
            bw.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder  builder = new StringBuilder();

            String line = "";

            while((line = br.readLine())!=null)
            {
                builder.append(line);
            }
            br.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return builder.toString();
        }
        catch(Exception ex)
        {
            //Log.d("Error",ex.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
        super.onPostExecute(s);
    }
}
