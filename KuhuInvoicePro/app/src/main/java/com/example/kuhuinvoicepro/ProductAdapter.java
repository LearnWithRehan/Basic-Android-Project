package com.example.kuhuinvoicepro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    List<ProductEntity> list;
    OnDeleteClick listener;

    public interface OnDeleteClick{
        void onDelete(ProductEntity product);
    }

    public ProductAdapter(List<ProductEntity> list, OnDeleteClick listener){
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_row,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProductEntity product = list.get(position);

        holder.tvName.setText(product.productName);

        holder.btnDelete.setOnClickListener(v -> {
            listener.onDelete(product);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        ImageView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvProductName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}