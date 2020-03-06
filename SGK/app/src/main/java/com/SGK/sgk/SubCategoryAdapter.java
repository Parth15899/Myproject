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

public class SubCategoryAdapter extends ArrayAdapter {
    public SubCategoryAdapter(Context context, int resource) {
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
        SubCategoryAdapter.SubCategoryHolder subCategoryHolder;

        if(row==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.showcategory,parent,false);
            subCategoryHolder = new SubCategoryAdapter.SubCategoryHolder();
            subCategoryHolder.tv1 = (TextView)row.findViewById(R.id.textCategory);


            subCategoryHolder.imv = (ImageView)row.findViewById(R.id.imageCategory);
            subCategoryHolder.imv.setClipToOutline(true);
            ViewGroup.LayoutParams layoutParams = SubCategoryHolder.imv.getLayoutParams();
            layoutParams.height=300;
            layoutParams.width=300;

            SubCategoryHolder.imv.setLayoutParams(layoutParams);
            row.setTag(subCategoryHolder);
        }
        else{
            subCategoryHolder=(SubCategoryAdapter.SubCategoryHolder)row.getTag();
        }
        SubCategoryClass subCategoryClass = (SubCategoryClass) this.getItem(position);
        subCategoryHolder.tv1.setText(subCategoryClass.getSubcatname());

        //contactHolder.rb1.setRating(Float.parseFloat("4.0"));

        String pimage = getContext().getResources().getString(R.string.imgsubcat) + subCategoryClass.getSubcatpic();

        Glide.with(getContext())
                .load(pimage)
                .placeholder(R.drawable.logo)
                .into(subCategoryHolder.imv);
        return row;
    }

    public static class SubCategoryHolder{

        TextView tv1;
        static ImageView imv;
    }
}
