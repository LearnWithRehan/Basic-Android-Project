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

//    @Insert
//    void insertInvoiceItems(List<InvoiceItemEntity> items);


    @Insert
    long insertInvoiceItem(InvoiceItemEntity item);

//    @Transaction
//    @Query("SELECT * FROM invoice_table ORDER BY id DESC")
//    List<InvoiceWithItems> getAllInvoicesWithItems();


    @Transaction
    @Query("SELECT * FROM invoice_table ORDER BY id DESC")
    List<InvoiceWithItems> getAllInvoicesWithItems();




    @Transaction
    @Query("SELECT * FROM invoice_table WHERE date BETWEEN :fromDate AND :toDate ORDER BY id DESC")
    List<InvoiceWithItems> getInvoicesByDate(String fromDate, String toDate);


    @Query("UPDATE invoice_table SET totalAmount = :totalAmount, discount = :discount, netAmount = :netAmount WHERE id = :invoiceId")
    void updateInvoiceTotals(int invoiceId, double totalAmount, double discount, double netAmount);

    @Query("DELETE FROM invoice_item_table WHERE itemId = :id")
    void deleteItemById(int id);



}