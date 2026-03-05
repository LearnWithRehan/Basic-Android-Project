package com.example.kuhuinvoicepro;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class InvoiceWithItems {

    @Embedded
    public InvoiceEntity invoice;

    @Relation(
            parentColumn = "id",
            entityColumn = "invoiceId"
    )
    public List<InvoiceItemEntity> items;
}