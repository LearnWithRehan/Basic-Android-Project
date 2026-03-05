package com.example.kuhuinvoicepro;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "invoice_table")
public class InvoiceEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String customerName;
    public String customerAddress;
    public String customerMobile;

    public double totalAmount;
    public double discount;
    public double netAmount;

    public String date;
}