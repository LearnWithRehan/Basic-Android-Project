package com.example.kuhuinvoicepro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InvoiceItemReportAdapter extends RecyclerView.Adapter<InvoiceItemReportAdapter.ViewHolder>{

    List<InvoiceItemEntity> list;

    public InvoiceItemReportAdapter(List<InvoiceItemEntity> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invoice_item_row,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        InvoiceItemEntity item = list.get(position);

        holder.tvProduct.setText(item.productName);
        holder.tvQty.setText(String.valueOf(item.qty));
        holder.tvRate.setText(String.valueOf(item.rate));
        holder.tvAmount.setText(String.valueOf(item.amount));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvProduct,tvQty,tvRate,tvAmount;

        public ViewHolder(View itemView) {
            super(itemView);

            tvProduct = itemView.findViewById(R.id.tvProduct);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }
}
