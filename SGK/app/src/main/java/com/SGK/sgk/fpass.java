package com.SGK.sgk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
public class fpass extends AppCompatActivity {

    Button btn;
    EditText et1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fpass);

        et1 = findViewById(R.id.fpcontact);
        btn = findViewById(R.id.btngetotp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact = et1.getText().toString().trim();
                if(contact.isEmpty() || contact.length()<10)
                {
                    et1.setError("Provide a Valid Contact Number");
                    et1.requestFocus();
                    return;
                }
                Intent intent = new Intent(fpass.this,verifyfpotp.class);
                intent.putExtra("contact",contact);
                startActivity(intent);
            }
        });
    }
}
