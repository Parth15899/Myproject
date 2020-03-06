package com.SGK.sgk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

public class OfferProductAdapter extends ArrayAdapter {


    public OfferProductAdapter(Context context, int resource) {
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;

        OfferProductAdapter.OfferHolder offerHolder;
        int pid;
        if(row==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.showofferproduct,parent,false);
            row.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT,600));
            offerHolder = new OfferProductAdapter.OfferHolder();
            offerHolder.tv1 = (TextView)row.findViewById(R.id.textofferProduct);
            offerHolder.tv2 = (TextView)row.findViewById(R.id.textPrice);
            offerHolder.tv3 = (TextView)row.findViewById(R.id.textofferPrice);

            offerHolder.iv1 = (ImageView)row.findViewById(R.id.imageofferProd);
            offerHolder.iv1.setClipToOutline(true);
            ViewGroup.LayoutParams layoutParams = offerHolder.iv1.getLayoutParams();
            layoutParams.height=250;
            layoutParams.width=250;

            offerHolder.iv1.setLayoutParams(layoutParams);
            row.setTag(offerHolder);
        }
        else{
            offerHolder=(OfferProductAdapter.OfferHolder)row.getTag();
        }
        OfferProductClass offerproductclass = (OfferProductClass)this.getItem(position);
        offerHolder.tv1.setText(offerproductclass.getPname());
        offerHolder.tv2.setText(String.valueOf(offerproductclass.getPprice()));
        offerHolder.tv2.setPaintFlags(offerHolder.tv2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        offerHolder.tv3.setText(String.valueOf(offerproductclass.getOfferprice()));
        //contactHolder.rb1.setRating(Float.parseFloat("4.0"));
        pid=offerproductclass.getPid();
        byte[] pimage = Base64.decode(offerproductclass.getPimage(),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(pimage,0,pimage.length);
        offerHolder.iv1.setImageBitmap(bitmap);
        return row;
    }

    public static class OfferHolder{
        TextView tv1,tv2,tv3;
        //RatingBar rb1;
        ImageView iv1;
        Button btn1;
    }
}
