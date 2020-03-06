package com.SGK.sgk;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;

public class productAdapter extends ArrayAdapter {

    public productAdapter(Context context, int resource)
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
        View row;
        row = convertView;





        ContactHolder contactHolder;
        int pid;

        if(row==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.showproduct,parent,false);
            row.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT,600));
            contactHolder = new ContactHolder();
            contactHolder.tv1 = (TextView)row.findViewById(R.id.textProduct);
            contactHolder.tv2 = (TextView)row.findViewById(R.id.textPrice);
            //contactHolder.rb1 = (RatingBar)row.findViewById(R.id.rb1);
            //contactHolder.btn1 = (Button)row.findViewById(R.id.btn1);
            //contactHolder.tv3 = (TextView)row.findViewById(R.id.textRating);
            contactHolder.iv1 = (ImageView)row.findViewById(R.id.imageProd);
            contactHolder.iv1.setClipToOutline(true);
            ViewGroup.LayoutParams layoutParams = contactHolder.iv1.getLayoutParams();
            layoutParams.height=250;
            layoutParams.width=250;

            contactHolder.iv1.setLayoutParams(layoutParams);
            row.setTag(contactHolder);
        }
        else{
            contactHolder=(ContactHolder)row.getTag();
        }
        productClass productclass = (productClass)this.getItem(position);
        contactHolder.tv1.setText(productclass.getPname());
        contactHolder.tv2.setText(productclass.getPprice());
        //contactHolder.rb1.setRating(Float.parseFloat("4.0"));
        pid=productclass.getPid();
        byte[] pimage = Base64.decode(productclass.getPimage(),Base64.DEFAULT);

        Glide.with(getContext())
                .load(pimage)
                .placeholder(R.drawable.logo)
                .into(contactHolder.iv1);

        //Bitmap bitmap = BitmapFactory.decodeByteArray(pimage,0,pimage.length);
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

