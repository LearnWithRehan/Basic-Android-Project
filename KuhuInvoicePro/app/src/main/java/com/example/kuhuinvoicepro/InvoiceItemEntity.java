package com.example.kuhuinvoicepro;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "invoice_item_table",
        foreignKeys = @ForeignKey(
                entity = InvoiceEntity.class,
                parentColumns = "id",
                childColumns = "invoiceId",
                onDelete = CASCADE
        )
)
public class InvoiceItemEntity {

    @PrimaryKey(autoGenerate = true)
    public int itemId;

    public int invoiceId;   // 🔥 Link to parent invoice

    public String productName;
    public double qty;
    public double rate;
    public double amount;
}