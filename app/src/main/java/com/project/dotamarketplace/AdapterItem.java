package com.project.dotamarketplace;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.project.dotamarketplace.model.Item;
import com.project.dotamarketplace.model.User;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.viewHolder> {
    private onItemListener onItemListener;
    ArrayList<Item> arrItem;
    Context ctx;

    public AdapterItem(ArrayList<Item> arrItem, Context ctx, onItemListener onItemListener) {
        this.arrItem = arrItem;
        this.ctx = ctx;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.list_item_view, parent,false);
        return new viewHolder(v, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Item item = arrItem.get(position);
        holder.textName.setText(item.getName());
        holder.textStock.setText("Stock: "+item.getStock());
        holder.textPrice.setText(item.getPrice()+"");
        String imageName = "image"+item.getItemId();
        int resID = ctx.getResources().getIdentifier(imageName, "drawable", ctx.getPackageName());
        holder.thumbnail.setImageResource(resID);
        holder.txtIsSelected.setVisibility(item.getIsVisible());
    }

    @Override
    public int getItemCount() {
        return arrItem.size();
    }



    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textName, textStock, textPrice, txtIsSelected;
        ImageView thumbnail;
        onItemListener onItemListener;


        public viewHolder(@NonNull View v, onItemListener onItemListener) {
            super(v);
            this.onItemListener = onItemListener;
            textName = v.findViewById(R.id.textName);
            textStock = v.findViewById(R.id.textStock);
            textPrice = v.findViewById(R.id.textPrice);
            thumbnail = v.findViewById(R.id.thumbnail);
            txtIsSelected = v.findViewById(R.id.txtIsSelected);

            this.onItemListener = onItemListener;
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Log.d("TAG", "onClick: adapter");
            onItemListener.onItemClicked(getAdapterPosition());
        }
    }
    public interface onItemListener{
        void onItemClicked(int position);
    }
}
