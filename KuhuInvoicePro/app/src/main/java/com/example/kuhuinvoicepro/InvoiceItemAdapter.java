package com.example.kuhuinvoicepro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InvoiceItemAdapter extends RecyclerView.Adapter<InvoiceItemAdapter.MyViewHolder>{

    List<InvoiceItemEntity> list;

    public InvoiceItemAdapter(List<InvoiceItemEntity> list){
        this.list = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView sr,product,qty,amount;

        public MyViewHolder(View v){
            super(v);

            sr=v.findViewById(R.id.tvSr);
            product=v.findViewById(R.id.tvProduct);
            qty=v.findViewById(R.id.tvQty);
            amount=v.findViewById(R.id.tvAmount);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_invoice_product,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,int position){

        InvoiceItemEntity item = list.get(position);

        holder.sr.setText(String.valueOf(position+1));
        holder.product.setText(item.productName);
        holder.qty.setText(String.valueOf(item.qty));
        holder.amount.setText(String.valueOf(item.amount));

    }

    @Override
    public int getItemCount(){
        return list.size();
    }
}