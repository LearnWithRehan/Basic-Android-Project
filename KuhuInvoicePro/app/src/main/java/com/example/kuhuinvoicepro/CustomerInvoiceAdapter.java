package com.example.kuhuinvoicepro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomerInvoiceAdapter extends RecyclerView.Adapter<CustomerInvoiceAdapter.ViewHolder>{

    List<InvoiceWithItems> list;

    public CustomerInvoiceAdapter(List<InvoiceWithItems> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        InvoiceEntity invoice = list.get(position).invoice;

        holder.text1.setText("Invoice : " + invoice.invoiceNo);
        holder.text2.setText("Date : " + invoice.date + "   ₹" + invoice.netAmount);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView text1,text2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}