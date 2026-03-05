package com.example.kuhuinvoicepro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {

    private List<InvoiceItem> itemList;
    private OnItemClickListener listener;

    // ✅ Custom Click Interface
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // ✅ Constructor
    public InvoiceAdapter(List<InvoiceItem> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_invoice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        InvoiceItem item = itemList.get(position);

        // ✅ Auto Serial Number
        holder.tvSr.setText(String.valueOf(position + 1));

        holder.tvProduct.setText(item.getProductName());
        holder.tvQty.setText(item.getQty() + " kg");
        holder.tvRate.setText(String.valueOf(item.getRate()));
        holder.tvAmount.setText(String.format("%.2f", item.getAmount()));

        // ✅ Item Click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // ✅ Clear All Items
    public void clearItems() {
        itemList.clear();
        notifyDataSetChanged();
    }

    // ✅ Remove Single Item
    public void removeItem(int position) {
        if (position >= 0 && position < itemList.size()) {
            itemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, itemList.size());
        }
    }

    // ✅ ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSr, tvProduct, tvQty, tvRate, tvAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSr = itemView.findViewById(R.id.tvSr);
            tvProduct = itemView.findViewById(R.id.tvProduct);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }
}