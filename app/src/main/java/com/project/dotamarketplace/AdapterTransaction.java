package com.project.dotamarketplace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.dotamarketplace.model.Item;
import com.project.dotamarketplace.model.Transaction;

import java.util.ArrayList;

public class AdapterTransaction extends RecyclerView.Adapter<AdapterTransaction.viewHolder>{
    ArrayList<Transaction> arrTransaction;
    Context ctx;
    DataHelper dataHelper;

    public AdapterTransaction(ArrayList<Transaction> arrTransaction, Context ctx) {
        this.arrTransaction = arrTransaction;
        this.ctx = ctx;
        dataHelper = new DataHelper(ctx);

    }

    @NonNull
    @Override
    public AdapterTransaction.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.list_transaction_view, parent,false);
        return new viewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder h, int position) {
        if (arrTransaction.size()<1){
            h.txtDate.setText("");
            h.textName.setText("NO TRANSACTION");
            h.textQuantity.setText("");
            h.textPrice.setText("");

        }else{
            Transaction transaction = arrTransaction.get(position);
            Item item = dataHelper.getItem(transaction.getItemID()+"");
            h.txtDate.setText(transaction.getTransactionDate());
            h.textName.setText(item.getName());
            h.textQuantity.setText("Quantity: " + transaction.getQuantity());
            h.textPrice.setText("Total: "+ transaction.getQuantity()*item.getPrice());
            String imageName = "image"+item.getItemId();
            int resID = ctx.getResources().getIdentifier(imageName, "drawable", ctx.getPackageName());
            h.thumbnail.setImageResource(resID);
        }
    }


    @Override
    public int getItemCount() {
        return arrTransaction.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView textName, textQuantity, textPrice, txtDate;
        ImageView thumbnail;

    public viewHolder(@NonNull View v) {
        super(v);
        txtDate = v.findViewById(R.id.txtTransactionDate);
        textName = v.findViewById(R.id.textName);
        textQuantity = v.findViewById(R.id.textQuantity);
        textPrice = v.findViewById(R.id.textPrice);
        thumbnail = v.findViewById(R.id.thumbnail);
    }
}
}
