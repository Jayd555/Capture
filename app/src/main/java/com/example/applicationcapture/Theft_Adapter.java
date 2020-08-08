package com.example.applicationcapture;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Theft_Adapter extends RecyclerView.Adapter<Theft_Adapter.MyViewHolder>{
    Context context;
    ArrayList<theft_data> theftData;

    public  Theft_Adapter(Context c,ArrayList<theft_data> theftData){
        context = c;
        this.theftData  = theftData;
    }

    @NonNull
    @Override
    public Theft_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Theft_Adapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.theft_row,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(Theft_Adapter.MyViewHolder holder, int position) {
        theft_data p = theftData.get(position);
        Picasso.get().load(p.getUrl()).fit().centerCrop().into(holder.imageview);

    }

    @Override
    public int getItemCount() {
        return theftData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageview;

        public MyViewHolder (View itemView){
            super(itemView);
            imageview = (ImageView) itemView.findViewById(R.id.iv_theft);

        }


    }
}

