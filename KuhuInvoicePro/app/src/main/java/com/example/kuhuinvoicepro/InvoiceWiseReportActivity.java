package com.example.kuhuinvoicepro;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InvoiceWiseReportActivity extends AppCompatActivity {

    AutoCompleteTextView etSearch;

    TextView tvCustomer,tvAddress,tvMobile,tvTotal,tvDiscount,tvNet;

    RecyclerView recyclerView;

    InvoiceDao invoiceDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_wise_report);

        etSearch = findViewById(R.id.etSearchInvoice);

        tvCustomer = findViewById(R.id.tvCustomer);
        tvAddress = findViewById(R.id.tvAddress);
        tvMobile = findViewById(R.id.tvMobile);
        tvTotal = findViewById(R.id.tvTotal);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvNet = findViewById(R.id.tvNet);

        recyclerView = findViewById(R.id.recyclerItems);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        invoiceDao = AppDatabase.getInstance(this).invoiceDao();

        // Suggestion Load
        List<String> invoiceList = invoiceDao.searchInvoice("");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line,
                        invoiceList);

        etSearch.setAdapter(adapter);

        etSearch.setOnItemClickListener((parent,view,position,id)->{

            String invoiceNo = parent.getItemAtPosition(position).toString();

            loadInvoice(invoiceNo);

        });

        etSearch.addTextChangedListener(new android.text.TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String typed = s.toString().trim();

                if(typed.isEmpty()){
                    clearInvoiceDetails();
                    return;
                }

                InvoiceWithItems data = invoiceDao.getInvoiceWithItems(typed);

                if(data == null){
                    clearInvoiceDetails();
                }

            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}

        });

    }

    private void loadInvoice(String invoiceNo){

        InvoiceWithItems data = invoiceDao.getInvoiceWithItems(invoiceNo);

        if(data==null) return;

        tvCustomer.setText(data.invoice.customerName);
        tvAddress.setText(data.invoice.customerAddress);
        tvMobile.setText(data.invoice.customerMobile);

        tvTotal.setText(""+data.invoice.totalAmount);
        tvDiscount.setText(""+data.invoice.discount);
        tvNet.setText(""+data.invoice.netAmount);

        InvoiceItemAdapter adapter = new InvoiceItemAdapter(data.items);

        recyclerView.setAdapter(adapter);

    }

    private void clearInvoiceDetails(){

        tvCustomer.setText("");
        tvAddress.setText("");
        tvMobile.setText("");

        tvTotal.setText("");
        tvDiscount.setText("");
        tvNet.setText("");

        recyclerView.setAdapter(null);

    }

}