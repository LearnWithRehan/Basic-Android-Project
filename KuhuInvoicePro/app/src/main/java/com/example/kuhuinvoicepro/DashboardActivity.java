package com.example.kuhuinvoicepro;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    RecyclerView recyclerDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recyclerDashboard = findViewById(R.id.recyclerDashboard);

        List<DashboardItem> list = new ArrayList<>();
        list.add(new DashboardItem("🧾", "Create Invoice"));
        list.add(new DashboardItem("📅", "Date Wise Report"));
        list.add(new DashboardItem("👤", "Customer Wise Report"));
        list.add(new DashboardItem("💰", "Monthly Total Sale"));
        list.add(new DashboardItem("📊", "Sales Dashboard"));
        list.add(new DashboardItem("📤", "Excel Export"));

        recyclerDashboard.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerDashboard.setAdapter(new DashboardAdapter(this, list));
    }
}