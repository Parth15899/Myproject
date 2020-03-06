package com.SGK.sgk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class changepass extends AppCompatActivity {

    EditText et1,et2;
    Button btn1;
    String contact;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);

        pb =findViewById(R.id.pbchangepass);
        btn1 = findViewById(R.id.changefppass);
        et1 = findViewById(R.id.newfppass);
        et2 = findViewById(R.id.fpconfirm);

        pb.setVisibility(View.GONE);
        contact = getIntent().getStringExtra("contact");

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pass = et1.getText().toString();
                String confirm = et2.getText().toString();

                if(pass.length()<8)
                {
                    et1.setError("Password must be 8 characters long!");
                    et1.requestFocus();
                    return;
                }
                if(!pass.equals(confirm)){
                    et2.setError("Passwords Don't Match");
                    et2.requestFocus();
                    return;
                }
                if(pass.isEmpty() || confirm.isEmpty())
                {
                    et1.setError("Field Required");
                    et2.setError("Field Required");
                    return;
                }
                new updatePassword().execute(contact,pass);
            }
        });

    }

    public class updatePassword extends AsyncTask<String,Void,String> {

        String u2="";

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
            u2=getResources().getString(R.string.connection)+"updatepassword.php";
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            String contact = strings[0];
            String password = strings[1];
            try{
                URL url= new URL(u2);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(outputStream));

                String data = URLEncoder.encode("contact","UTF-8")+"="+URLEncoder.encode(contact,"UTF-8");
                data+="&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

                bw.write(data);
                bw.flush();
                bw.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder builder = new StringBuilder();
                String line = "";

                while ((line = br.readLine()) != null) {
                    builder.append(line);
                    //Log.d("Connect3","Done");
                }

                br.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return builder.toString();
            }
            catch(Exception ex)
            {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            pb.setVisibility(View.GONE);
            //Toast.makeText(DeliveryProducts.this, s, Toast.LENGTH_LONG).show();
            if(s.equals("Updated"))
            {
                Toast.makeText(changepass.this, "Password Changed, Login Again!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(changepass.this,Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else{
                Toast.makeText(changepass.this, s, Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }
    }
}
