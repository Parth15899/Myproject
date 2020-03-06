package com.SGK.sgk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;

public class CartAdapter extends ArrayAdapter {

    UpdateCart obj;
    RemoveCart obj1;
    SharedPreferences sharedPreferences;
    private final String MyPrefs = "MyPrefs";

    public CartAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void add(Object object) {
        super.add(object);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        sharedPreferences = getContext().getSharedPreferences(MyPrefs,Context.MODE_PRIVATE);

        obj = new UpdateCart(getContext());
        obj1 = new RemoveCart(getContext());
        View row;
        row = convertView;

        CartAdapter.CartHolder cartHolder;
        int pid;
        if(row==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.cartlist,parent,false);
            cartHolder = new CartAdapter.CartHolder();
            cartHolder.tv1 = (TextView)row.findViewById(R.id.pnamecart);
            cartHolder.tv2 = (TextView)row.findViewById(R.id.ppricecart);
            cartHolder.tv3 = (TextView)row.findViewById(R.id.tvcartquant);
            cartHolder.tv4 = (TextView)row.findViewById(R.id.ptotalpricecart);
            cartHolder.btn2 = (Button)row.findViewById(R.id.btncartplus);
            cartHolder.btn1 = (Button)row.findViewById(R.id.btncartminus);
            cartHolder.btn3 = (Button)row.findViewById(R.id.removeItem);

            cartHolder.iv1 = (ImageView)row.findViewById(R.id.imagecart);
            cartHolder.iv1.setClipToOutline(true);
            ViewGroup.LayoutParams layoutParams = cartHolder.iv1.getLayoutParams();
            layoutParams.height=300;
            layoutParams.width=300;

            cartHolder.iv1.setLayoutParams(layoutParams);
            row.setTag(cartHolder);
        }
        else{
            cartHolder=(CartAdapter.CartHolder)row.getTag();
        }
        final CartClass cartClass = (CartClass) this.getItem(position);
        cartHolder.tv1.setText(cartClass.getPname());
        cartHolder.tv2.setText(String.valueOf(cartClass.getPrice()));
        cartHolder.tv3.setText(String.valueOf(cartClass.getQuant()));
        cartHolder.tv4.setText(String.valueOf(cartClass.getTotalprice()));

        cartHolder.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pid = cartClass.getPid();
                int user = cartClass.getUserid();
                int quant = cartClass.getQuant();
                if(quant>1)
                {
                    quant = quant-1;
                    int totprice;
                    int price = cartClass.getPrice();
                    totprice = price * quant;
                    obj.execute(sharedPreferences.getString("Id",""),String.valueOf(pid),String.valueOf(quant),String.valueOf(totprice));
                    Intent intent = new Intent(getContext(),ShoppingCart.class);
                    ((Activity)getContext()).finish();
                    ((Activity)getContext()).overridePendingTransition(0,0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getContext().startActivity(intent);
                    ((Activity)getContext()).overridePendingTransition(0,0);
                }
                else{
                    Toast.makeText(getContext(), "Quantity Cannot be less than 1", Toast.LENGTH_SHORT).show();
                }





            }
        });

        cartHolder.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pid = cartClass.getPid();
                int user = cartClass.getUserid();
                int quant = cartClass.getQuant();
                if(quant<10)
                {
                    quant = quant+1;
                    int totprice;
                    int price = cartClass.getPrice();
                    totprice = price * quant;
                    obj.execute(sharedPreferences.getString("Id",""),String.valueOf(pid),String.valueOf(quant),String.valueOf(totprice));
                    Intent intent = new Intent(getContext(),ShoppingCart.class);
                    ((Activity)getContext()).finish();
                    ((Activity)getContext()).overridePendingTransition(0,0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getContext().startActivity(intent);
                    ((Activity)getContext()).overridePendingTransition(0,0);
                }
                else{
                    Toast.makeText(getContext(), "Quantity Cannot be more than 10", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cartHolder.btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pid = cartClass.getPid();
                int user = cartClass.getUserid();

                obj1.execute(sharedPreferences.getString("Id",""),String.valueOf(pid));
                Intent intent = new Intent(getContext(),ShoppingCart.class);
                ((Activity)getContext()).finish();
                ((Activity)getContext()).overridePendingTransition(0,0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getContext().startActivity(intent);
                ((Activity)getContext()).overridePendingTransition(0,0);

            }
        });


        //contactHolder.rb1.setRating(Float.parseFloat("4.0"));

        String pimage = getContext().getResources().getString(R.string.imgprod) + cartClass.getPimage();
        Glide.with(getContext())
                .load(pimage)
                .placeholder(R.drawable.logo)

                .into(cartHolder.iv1);
        return row;
    }

    public static class CartHolder{
        TextView tv1,tv2,tv3,tv4;
        Button btn1,btn2,btn3;
        ImageView iv1;

    }
}
