package com.example.kuhuinvoicepro;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;


@Dao
public interface InvoiceDao {

    @Insert
    long insertInvoice(InvoiceEntity invoice);

    @Insert
    void insertInvoiceItems(List<InvoiceItemEntity> items);

    @Transaction
    @Query("SELECT * FROM invoice_table ORDER BY id DESC")
    List<InvoiceWithItems> getAllInvoicesWithItems();



    @Transaction
    @Query("SELECT * FROM invoice_table WHERE date BETWEEN :fromDate AND :toDate ORDER BY id DESC")
    List<InvoiceWithItems> getInvoicesByDate(String fromDate, String toDate);


}