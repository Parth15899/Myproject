package com.SGK.sgk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;

public class UndisOrderAdapter extends ArrayAdapter {
    SharedPreferences sharedPreferences;
    private final String MyPrefs = "MyPrefs";
    int grandtotal,total;

    public UndisOrderAdapter(Context context, int resource) {
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
        sharedPreferences = getContext().getSharedPreferences(MyPrefs,Context.MODE_PRIVATE);


        View row;
        row = convertView;

        UndisOrderAdapter.UndisOrderHolder undisOrderHolder;

        if(row==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.undisorder,parent,false);
            undisOrderHolder = new UndisOrderAdapter.UndisOrderHolder();
            undisOrderHolder.tv1 = (TextView)row.findViewById(R.id.pnameundisorder);
            undisOrderHolder.tv2 = (TextView)row.findViewById(R.id.ppriceundisorder);
            undisOrderHolder.tv3 = (TextView)row.findViewById(R.id.ptotalundisorder);
            undisOrderHolder.btn1 = (Button)row.findViewById(R.id.cancelorder);

            undisOrderHolder.iv1 = (ImageView)row.findViewById(R.id.imageundisorder);
            undisOrderHolder.iv1.setClipToOutline(true);
            ViewGroup.LayoutParams layoutParams = undisOrderHolder.iv1.getLayoutParams();
            layoutParams.height=300;
            layoutParams.width=300;

            undisOrderHolder.iv1.setLayoutParams(layoutParams);
            row.setTag(undisOrderHolder);
        }
        else{
            undisOrderHolder=(UndisOrderAdapter.UndisOrderHolder)row.getTag();
        }
        final UndisOrderClass undisOrderClass = (UndisOrderClass) this.getItem(position);
        undisOrderHolder.tv1.setText(undisOrderClass.getPname());
        undisOrderHolder.tv2.setText(String.valueOf(undisOrderClass.getPrice()));
        undisOrderHolder.tv3.setText(String.valueOf(undisOrderClass.getTotal()));

        undisOrderHolder.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int pid = undisOrderClass.getPid();
                //final int user = undisOrderClass.getUserid();
                final int quant = undisOrderClass.getQuant();
                final int orderid = undisOrderClass.getOrderid();

                total = undisOrderClass.getTotal();
                grandtotal = grandtotal-total;
                AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                ab.setTitle("Cancel Order");
                ab.setIcon(R.drawable.logsgk);
                ab.setMessage("Are you sure you want to cancel this Product?");
                ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new CancelOrder(getContext()).execute(sharedPreferences.getString("Id",""),String.valueOf(pid),String.valueOf(quant),String.valueOf(orderid),String.valueOf(grandtotal));
                        Intent intent = new Intent(getContext(),UndispatchOrder.class);
                        ((Activity)getContext()).finish();
                        ((Activity)getContext()).overridePendingTransition(0,0);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getContext().startActivity(intent);
                        ((Activity)getContext()).overridePendingTransition(0,0);

                    }
                });

                ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog ad = ab.create();
                ad.show();

            }
        });


        //contactHolder.rb1.setRating(Float.parseFloat("4.0"));

        String pimage = getContext().getResources().getString(R.string.imgprod) + undisOrderClass.getPimage();
        Glide.with(getContext())
                .load(pimage)
                .placeholder(R.drawable.logo)

                .into(undisOrderHolder.iv1);
        return row;

    }

    public static class UndisOrderHolder{
        TextView tv1,tv2,tv3;
        Button btn1;
        ImageView iv1;
    }
}
