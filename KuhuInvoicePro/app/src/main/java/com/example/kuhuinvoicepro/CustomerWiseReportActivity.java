package com.example.kuhuinvoicepro;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomerWiseReportActivity extends AppCompatActivity {

    AutoCompleteTextView autoCustomer;
    TextView tvCustomerDetails;
    RecyclerView recyclerCustomerInvoices;

    InvoiceDao invoiceDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_wise_report);

        autoCustomer = findViewById(R.id.autoCustomer);
        tvCustomerDetails = findViewById(R.id.tvCustomerDetails);
        recyclerCustomerInvoices = findViewById(R.id.recyclerCustomerInvoices);

        invoiceDao = AppDatabase.getInstance(this).invoiceDao();

        loadCustomerSuggestions();
        autoCustomer.setOnItemClickListener((parent, view, position, id) -> {

            String name = parent.getItemAtPosition(position).toString();

            loadCustomerData(name);
            loadCustomerProducts(name);

        });
    }

    private void loadCustomerSuggestions() {

        new Thread(() -> {

            List<String> customers = invoiceDao.getAllCustomerNames();

            runOnUiThread(() -> {

                ArrayAdapter<String> adapter =
                        new ArrayAdapter<>(this,
                                android.R.layout.simple_dropdown_item_1line,
                                customers);

                autoCustomer.setAdapter(adapter);

            });

        }).start();

    }

    private void loadCustomerData(String name) {

        new Thread(() -> {

            List<InvoiceWithItems> invoices = invoiceDao.getInvoicesByCustomer(name);

            runOnUiThread(() -> {

                if(invoices.size() > 0){

                    InvoiceEntity invoice = invoices.get(0).invoice;

                    tvCustomerDetails.setText(
                            "Name : " + invoice.customerName +
                                    "\nMobile : " + invoice.customerMobile +
                                    "\nAddress : " + invoice.customerAddress
                    );

                }

                recyclerCustomerInvoices.setLayoutManager(
                        new LinearLayoutManager(this));

                recyclerCustomerInvoices.setAdapter(
                        new CustomerInvoiceAdapter(invoices));

            });

        }).start();

    }


    private void loadCustomerProducts(String name){

        new Thread(() -> {

            List<CustomerProductReport> list =
                    invoiceDao.getCustomerProductReport(name);

            runOnUiThread(() -> {

                recyclerCustomerInvoices.setLayoutManager(
                        new LinearLayoutManager(this));

                recyclerCustomerInvoices.setAdapter(
                        new CustomerProductAdapter(list));

            });

        }).start();

    }


}