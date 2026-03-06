package com.example.kuhuinvoicepro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class CustomerProductAdapter extends RecyclerView.Adapter<CustomerProductAdapter.ViewHolder>{

    List<CustomerProductReport> list;

    public CustomerProductAdapter(List<CustomerProductReport> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer_product,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CustomerProductReport item = list.get(position);

        holder.name.setText(item.customerName);
        holder.mobile.setText(item.customerMobile);
        holder.address.setText(item.customerAddress);

        holder.date.setText(item.date);
        holder.product.setText(item.productName);
        holder.qty.setText(item.qty + " Kg");
        holder.amount.setText("₹ " + item.amount);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,mobile,address,date,product,qty,amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tvName);
            mobile = itemView.findViewById(R.id.tvMobile);
            address = itemView.findViewById(R.id.tvAddress);

            date = itemView.findViewById(R.id.tvDate);
            product = itemView.findViewById(R.id.tvProduct);
            qty = itemView.findViewById(R.id.tvQty);
            amount = itemView.findViewById(R.id.tvAmount);
        }
    }
}