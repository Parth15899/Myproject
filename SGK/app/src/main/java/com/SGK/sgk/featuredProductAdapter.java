package com.SGK.sgk;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;

public class featuredProductAdapter extends ArrayAdapter {
    SharedPreferences sharedPreferences;
    private final String MyPrefs = "MyPrefs";

    addtocart obj;

    public featuredProductAdapter(Context context, int resource)
    {
        super(context,resource);
    }

    @Override
    public void add(Object object) {
        super.add(object);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        sharedPreferences = getContext().getSharedPreferences(MyPrefs,Context.MODE_PRIVATE);
        View row;
        row = convertView;
        ContactHolder contactHolder;
        final int pid;
        final int quant;
        final String price;
        final int changeprice;
        final int userid;
        final int totalprice;
        if(row==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.showfeatureproduct,parent,false);
            contactHolder = new ContactHolder();
            contactHolder.tv1 = (TextView)row.findViewById(R.id.textFeatureProduct);
            contactHolder.tv2 = (TextView)row.findViewById(R.id.textFeaturePrice);
            contactHolder.btn1 = (Button)row.findViewById(R.id.featureAdd);
            //contactHolder.rb1 = (RatingBar)row.findViewById(R.id.rb1);
            //contactHolder.btn1 = (Button)row.findViewById(R.id.btn1);
            //contactHolder.tv3 = (TextView)row.findViewById(R.id.textRating);
            contactHolder.iv1 = (ImageView)row.findViewById(R.id.imageFeatureProd);
            contactHolder.iv1.setClipToOutline(true);
            ViewGroup.LayoutParams layoutParams = contactHolder.iv1.getLayoutParams();
            layoutParams.height=200;
            layoutParams.width=200;

            contactHolder.iv1.setLayoutParams(layoutParams);
            row.setTag(contactHolder);
        }
        else{
            contactHolder=(ContactHolder)row.getTag();
        }
        productClass productclass = (productClass)this.getItem(position);
        contactHolder.tv1.setText(productclass.getPname());
        contactHolder.tv2.setText(productclass.getPprice());

        pid=productclass.getPid();
        quant = productclass.getQuant();
        userid = 1;
        price = productclass.getPprice();
        changeprice = Integer.parseInt(price);
        totalprice = changeprice*quant;


        contactHolder.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //obj = new addtocart(getContext());
                new addtocart(getContext()).execute(sharedPreferences.getString("Id",""),String.valueOf(pid),String.valueOf(quant),String.valueOf(price),String.valueOf(totalprice));
                //Toast.makeText(getContext(), "Pid : "+pid+" userid : "+userid+" price : "+String.valueOf(changeprice)+" quant : "+quant+" Total : "+totalprice, Toast.LENGTH_SHORT).show();
            }
        });

        byte[] pimage = Base64.decode(productclass.getPimage(),Base64.DEFAULT);
        Glide.with(getContext())
                .load(pimage)
                .placeholder(R.drawable.logo)
                .into(contactHolder.iv1);
        //Bitmap bitmap = BitmapFactory.decodeByteArray(pimage,0,pimage.length);
        //bitmap = Bitmap.createScaledBitmap(bitmap,520,520,false);

        //contactHolder.iv1.setImageBitmap(bitmap);
        return row;
    }

    public static class ContactHolder{
        TextView tv1,tv2,tv3;
        //RatingBar rb1;
        ImageView iv1;
        Button btn1;
    }
}

