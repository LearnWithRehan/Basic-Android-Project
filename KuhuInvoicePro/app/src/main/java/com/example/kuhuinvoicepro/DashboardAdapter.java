package com.example.kuhuinvoicepro;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    Context context;
    List<DashboardItem> list;

    public DashboardAdapter(Context context, List<DashboardItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DashboardItem item = list.get(position);

        holder.tvIcon.setText(item.getIcon());
        holder.tvTitle.setText(item.getTitle());

        holder.itemView.setOnClickListener(v -> {

            if (position == 0) {
                // Create Invoice
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }

            else if (position == 1) {
                // Date Report
                Intent intent = new Intent(context, DateReportActivity.class);
                context.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvIcon, tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvIcon = itemView.findViewById(R.id.tvIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}