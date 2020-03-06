package com.SGK.sgk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SignUp extends AppCompatActivity {

    TextView tv;
    Button btn1;
    EditText et1,et2,et3,et4,et5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btn1 = findViewById(R.id.buttonsignup);
        tv = findViewById(R.id.tvLogin);
        et1 = findViewById(R.id.namesignup);
        et2 = findViewById(R.id.emailsignup);
        et3 = findViewById(R.id.contactsignup);
        et4 = findViewById(R.id.passwordsignup);
        et5 = findViewById(R.id.passwordsignupconfirm);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this,Login.class));
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact = et3.getText().toString().trim();
                String name = et1.getText().toString();
                String email = et2.getText().toString();
                String password = et4.getText().toString();
                String confirm = et5.getText().toString();
                if(name.isEmpty())
                {
                    et1.setError("Mandatory Field!");
                    et1.requestFocus();
                    return;
                }
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

// onClick of button perform this simplest code.
                if(!email.matches(emailPattern))
                {
                    et2.setError("Enter a valid Email");
                    et2.requestFocus();
                    return;
                }
                if(contact.isEmpty() || contact.length()<10)
                {
                    et3.setError("Please Provide a Valid Contact");
                    et3.requestFocus();
                    return;
                }
                if(password.isEmpty() || password.length()<8)
                {
                    et4.setError("Password must be 8 characters long!");
                    et4.requestFocus();
                    return;
                }
                if(!password.equals(confirm)){
                    et2.setError("Passwords Don't Match");
                    et2.requestFocus();
                    return;
                }
                if(password.isEmpty() || confirm.isEmpty())
                {
                    et1.setError("Field Required");
                    et2.setError("Field Required");
                    return;
                }
                new signUser().execute(contact);
            }
        });
    }

    public class signUser extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


                String contact = params[0];
                String u1 = getResources().getString(R.string.connection)+"validateUser.php";

                try{
                    URL url = new URL(u1);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String data = URLEncoder.encode("contact","UTF-8")+"="+URLEncoder.encode(contact,"UTF-8");

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
            if(s.equals("Welcome")){
                Intent intent = new Intent(SignUp.this,verifysignotp.class);
                String contact = et3.getText().toString().trim();
                String name = et1.getText().toString();
                String email = et2.getText().toString();

                String password = et4.getText().toString();
                //intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);

                intent.putExtra("name",name);
                intent.putExtra("email",email);
                intent.putExtra("contact",contact);
                intent.putExtra("password",password);

                startActivity(intent);
            }
            else{
                Toast.makeText(SignUp.this, s, Toast.LENGTH_LONG).show();
            }

            super.onPostExecute(s);
        }
    }
}
