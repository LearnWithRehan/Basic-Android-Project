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
        holder.txtDate.setText("Date : " + data.invoice.date);
        holder.txtInvoice.setText("InvoiceNo: " + data.invoice.invoiceNo);
        holder.txtCustomer.setText("Customer : " + data.invoice.customerName);
        holder.txtAddress.setText("Address: " + data.invoice.customerAddress);
        holder.txtMobile.setText("Mobile: " + data.invoice.customerMobile);
        holder.txtNetAmt.setText("Total Amount: " + data.invoice.totalAmount);
        holder.txtDiscount.setText("Discount: " + data.invoice.discount);
        holder.txtAmount.setText("Net Amount : ₹ " + data.invoice.netAmount);

        StringBuilder items = new StringBuilder();

        for (InvoiceItemEntity item : data.items) {

            items.append(item.productName)
                    .append(" | Qty: ")
                    .append(item.qty +"kg")
//                    .append(" | Rate: ")
//                    .append(item.rate)
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

        TextView txtInvoice,txtCustomer,txtAddress,txtMobile,txtNetAmt, txtDate, txtAmount,txtDiscount, txtItems;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtInvoice = itemView.findViewById(R.id.txtInvoice);
            txtCustomer = itemView.findViewById(R.id.txtCustomer);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtMobile = itemView.findViewById(R.id.txtMobile);
            txtNetAmt = itemView.findViewById(R.id.txtNetAmt);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtDiscount = itemView.findViewById(R.id.txtDisconut);
            txtItems = itemView.findViewById(R.id.txtItems);
        }
    }
}