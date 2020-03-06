 package com.SGK.sgk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class opadapter extends RecyclerView.Adapter<opadapter.ViewHolder> {

    public ArrayList<OfferProductClass> myList;
    Context ctx;

    SharedPreferences sharedPreferences;
    private final String MyPrefs = "MyPrefs";

    public opadapter(Context context,ArrayList<OfferProductClass> myList)
    {
        this.ctx = context;
        this.myList = myList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v  = inflater.inflate(R.layout.showofferproduct,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(myList.get(position));
        TextView tvname = holder.tv1;
        TextView tvprice = holder.tv2;
        TextView tvoffer = holder.tv3;

        ImageView iv1 = holder.iv1;
        Button addbtn = holder.btn1;
        addbtn.setTag(myList.get(position));

        tvname.setText(myList.get(position).getPname());
        tvprice.setText(String.valueOf(myList.get(position).getPprice()));
        tvoffer.setText(String.valueOf(myList.get(position).getOfferprice()));
        tvprice.setPaintFlags(tvprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        String pimage = ctx.getResources().getString(R.string.imgprod) + myList.get(position).getPimage();
        Glide.with(ctx)
                .load(pimage)
                .placeholder(R.drawable.logo)

                .into(iv1);
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv1;
        public TextView tv2,tv3;
        public ImageView iv1;
        public Button btn1;
        public View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sharedPreferences = itemView.getContext().getSharedPreferences(MyPrefs,Context.MODE_PRIVATE);
            layout = itemView;
            tv1 =(TextView)itemView.findViewById(R.id.textofferProduct);
            tv2 =(TextView)itemView.findViewById(R.id.textPrice);
            tv3 =(TextView)itemView.findViewById(R.id.textofferPrice);
            iv1 =(ImageView)itemView.findViewById(R.id.imageofferProd);
            btn1 = (Button)itemView.findViewById(R.id.btnAdd);

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OfferProductClass pc =(OfferProductClass)view.getTag();

                    int quant = 1;
                    String price = String.valueOf(pc.getOfferprice());
                    int changeprice = Integer.parseInt(price);
                    int totalprice = changeprice*quant;

                    new addtocart(view.getContext()).execute(sharedPreferences.getString("Id",""),String.valueOf(pc.getPid()),String.valueOf(quant),String.valueOf(price),String.valueOf(totalprice));
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OfferProductClass pc =(OfferProductClass)view.getTag();
                    Intent intent = new Intent(view.getContext(),productDetails.class);
                    intent.putExtra("pid",String.valueOf(pc.getPid()));
                    intent.putExtra("pprice",String.valueOf(pc.getOfferprice()));
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
