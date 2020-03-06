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

public class CancelOrder extends AsyncTask<String,Void,String> {

    String u1 = "";
    Context ctx;
    public CancelOrder(Context context) {
        this.ctx=context;
    }

    @Override
    protected void onPreExecute() {
        u1 = ctx.getResources().getString(R.string.connection)+"cancelorder.php";
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String userid = strings[0];
        String pid = strings[1];
        String quant = strings[2];
        String orderid = strings[3];
        String grandtotal = strings[4];


        try{
            URL url=new URL(u1);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            //Log.d("Connect","Done");

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

            String data = URLEncoder.encode("pid","UTF-8")+"="+URLEncoder.encode(pid,"UTF-8");
            data+="&"+URLEncoder.encode("quant","UTF-8")+"="+URLEncoder.encode(quant,"UTF-8");
            data+="&"+URLEncoder.encode("userid","UTF-8")+"="+URLEncoder.encode(userid,"UTF-8");
            data+="&"+URLEncoder.encode("orderid","UTF-8")+"="+URLEncoder.encode(orderid,"UTF-8");
            data+="&"+URLEncoder.encode("grandtotal","UTF-8")+"="+URLEncoder.encode(grandtotal,"UTF-8");

            bw.write(data);

            bw.flush();
            bw.close();

            try {
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                //Log.d("Connect2","Done");
                StringBuilder builder = new StringBuilder();
                String line = "";

                while ((line = br.readLine()) != null) {
                    builder.append(line + "\n");
                    //Log.d("Connect3","Done");
                }

                br.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return builder.toString();
            }
            catch (Exception ex){

            }
        }
        catch(Exception ex)
        {

        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
        super.onPostExecute(s);
    }
}
