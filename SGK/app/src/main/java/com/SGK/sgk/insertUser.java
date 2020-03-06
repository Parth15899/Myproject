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

public class insertUser extends AsyncTask<String, Void,String> {

    Context ctx;

    public insertUser(Context context)
    {
        this.ctx = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String method=params[0];
        if(method.equals("signup"))
        {
            String name = params[1];
            String email = params[2];
            String contact = params[3];
            String password = params[4];
            String u1 = ctx.getResources().getString(R.string.connection)+"insertUser.php";

            try{
                URL url = new URL(u1);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8");
                data+="&"+URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8");
                data+="&"+URLEncoder.encode("contact","UTF-8")+"="+URLEncoder.encode(contact,"UTF-8");
                data+="&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

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
        }


        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();

        super.onPostExecute(s);
    }
}
