package com.SGK.sgk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;

public class CategoryAdapter extends ArrayAdapter {
    public CategoryAdapter(Context context, int resource) {
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
        CategoryAdapter.CategoryHolder categoryHolder;
        int pid;
        if(row==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.showcategory,parent,false);
            categoryHolder = new CategoryAdapter.CategoryHolder();
            categoryHolder.tv1 = (TextView)row.findViewById(R.id.textCategory);


            categoryHolder.iv1 = (ImageView)row.findViewById(R.id.imageCategory);
            categoryHolder.iv1.setClipToOutline(true);
            ViewGroup.LayoutParams layoutParams = categoryHolder.iv1.getLayoutParams();
            layoutParams.height=300;
            layoutParams.width=300;

            categoryHolder.iv1.setLayoutParams(layoutParams);
            row.setTag(categoryHolder);
        }
        else{
            categoryHolder=(CategoryAdapter.CategoryHolder)row.getTag();
        }
        CategoryClass categoryClass = (CategoryClass) this.getItem(position);
        categoryHolder.tv1.setText(categoryClass.getCatname());

        //contactHolder.rb1.setRating(Float.parseFloat("4.0"));

        String pimage = getContext().getResources().getString(R.string.imgcat) + categoryClass.getCatpic();

        Glide.with(getContext())
                .load(pimage)
                .placeholder(R.drawable.logo)
                .into(categoryHolder.iv1);
        return row;


    }

    public static class CategoryHolder{
        TextView tv1;

        ImageView iv1;

    }
}
