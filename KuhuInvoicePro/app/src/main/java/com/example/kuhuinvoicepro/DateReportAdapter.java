package com.example.kuhuinvoicepro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DateReportAdapter extends RecyclerView.Adapter<DateReportAdapter.ViewHolder> {

    List<InvoiceWithItems> list;

    public DateReportAdapter(List<InvoiceWithItems> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_invoice_report, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        InvoiceWithItems data = list.get(position);

        holder.txtCustomer.setText("Customer : " + data.invoice.customerName);
        holder.txtDate.setText("Date : " + data.invoice.date);
        holder.txtAmount.setText("Net Amount : ₹ " + data.invoice.netAmount);

        StringBuilder items = new StringBuilder();

        for (InvoiceItemEntity item : data.items) {

            items.append(item.productName)
                    .append(" | Qty: ")
                    .append(item.qty)
                    .append(" | ₹")
                    .append(item.amount)
                    .append("\n");

        }

        holder.txtItems.setText(items.toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCustomer, txtDate, txtAmount, txtItems;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCustomer = itemView.findViewById(R.id.txtCustomer);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtItems = itemView.findViewById(R.id.txtItems);
        }
    }
}