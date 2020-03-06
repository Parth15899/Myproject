package com.SGK.sgk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

public class ContactUs extends AppCompatActivity {

    CardView cv1,cv2,cv3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        cv1 = findViewById(R.id.cvcall);
        cv2 = findViewById(R.id.cvmail);
        cv3 = findViewById(R.id.cvaddress);

        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(ContactUs.this,R.anim.click));
                String contact = "7624086268";
                Uri u = Uri.parse("tel:"+contact);
                Intent intent = new Intent(Intent.ACTION_DIAL,u);
                startActivity(intent);
            }
        });

        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(ContactUs.this,R.anim.click));
                String email="sgkdryfruit@gmail.com";
                String[]to={email};

                String subject="Customer Feedback";

                String body="";

                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mail to:"));
                intent.setType("text/plain");

                intent.putExtra(Intent.EXTRA_EMAIL,to);
                intent.putExtra(Intent.EXTRA_SUBJECT,subject);
                intent.putExtra(Intent.EXTRA_TEXT,body);

                Intent chooser=Intent.createChooser(intent,"Send To");

                startActivity(intent);
                finish();
            }
        });

        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(ContactUs.this,R.anim.click));

                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("geo:22.3165821,73.1672154?q=SGK+DRYFRUIT'S+(SATGURU+KRUPA+DRYFRUIT+AND+NUTS)"));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });
    }
}
