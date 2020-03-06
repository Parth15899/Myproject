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

public class DeliveredOrderAdapter extends ArrayAdapter {
    public DeliveredOrderAdapter(Context context, int resource) {
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
        int disp,del;
        View row;
        row = convertView;

        DeliveredOrderAdapter.DelOrderHolder delOrderHolder;
        int pid;
        if(row==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.delorder,parent,false);
            delOrderHolder = new DeliveredOrderAdapter.DelOrderHolder();
            delOrderHolder.tv1 = (TextView)row.findViewById(R.id.pnamedelorder);
            delOrderHolder.tv2 = (TextView)row.findViewById(R.id.ppricedelorder);
            delOrderHolder.tv3 = (TextView)row.findViewById(R.id.quantdelorder);
            delOrderHolder.tv4 = (TextView)row.findViewById(R.id.totalpricedelorder);


            delOrderHolder.iv1 = (ImageView)row.findViewById(R.id.imageorderdel);
            delOrderHolder.iv2 = (ImageView)row.findViewById(R.id.imgdisp);
            delOrderHolder.iv3 = (ImageView)row.findViewById(R.id.imgdel);
            delOrderHolder.iv1.setClipToOutline(true);
            ViewGroup.LayoutParams layoutParams = delOrderHolder.iv1.getLayoutParams();
            layoutParams.height=300;
            layoutParams.width=300;

            delOrderHolder.iv1.setLayoutParams(layoutParams);
            row.setTag(delOrderHolder);
        }
        else{
            delOrderHolder=(DeliveredOrderAdapter.DelOrderHolder)row.getTag();
        }
        final DeliveredOrderClass deliveredOrderClass = (DeliveredOrderClass) this.getItem(position);
        delOrderHolder.tv1.setText(deliveredOrderClass.getPname());
        delOrderHolder.tv2.setText(String.valueOf(deliveredOrderClass.getPrice()));
        delOrderHolder.tv3.setText(String.valueOf("Quantity : "+deliveredOrderClass.getQuant()));
        delOrderHolder.tv4.setText(String.valueOf("Total : "+deliveredOrderClass.getTotal()));

        disp = deliveredOrderClass.getDispatch();
        del = deliveredOrderClass.getDelivered();

        if(del==0){
            delOrderHolder.iv3.setVisibility(View.GONE);
        }
        else{
            delOrderHolder.iv3.setVisibility(View.VISIBLE);
            delOrderHolder.iv2.setVisibility(View.GONE);
        }

        String pimage = getContext().getResources().getString(R.string.imgprod) + deliveredOrderClass.getPimage();
        Glide.with(getContext())
                .load(pimage)
                .placeholder(R.drawable.logo)

                .into(delOrderHolder.iv1);
        return row;
    }

    public static class DelOrderHolder{
        ImageView iv1,iv2,iv3;
        TextView tv1,tv2,tv3,tv4;
    }
}
