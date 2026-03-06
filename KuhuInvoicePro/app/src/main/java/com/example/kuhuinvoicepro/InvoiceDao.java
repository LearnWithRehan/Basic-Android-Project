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




    @Query("SELECT DISTINCT customerName FROM invoice_table")
    List<String> getAllCustomerNames();


    @Transaction
    @Query("SELECT * FROM invoice_table WHERE customerName = :name ORDER BY id DESC")
    List<InvoiceWithItems> getInvoicesByCustomer(String name);


    @Transaction
    @Query("SELECT invoice_table.date AS date, " +
            "invoice_item_table.productName AS productName, " +
            "invoice_item_table.qty AS qty, " +
            "invoice_item_table.amount AS amount " +
            "FROM invoice_table " +
            "INNER JOIN invoice_item_table " +
            "ON invoice_table.id = invoice_item_table.invoiceId " +
            "WHERE invoice_table.customerName = :customerName " +
            "ORDER BY invoice_table.date DESC")
    List<CustomerProductReport> getCustomerProductReport(String customerName);

//
//    @Query("SELECT DISTINCT customerName FROM invoice_table WHERE customerName LIKE :name || '%'")
//    List<String> searchCustomerNames(String name);



    @Query("SELECT invoice_table.date, invoice_table.customerName, invoice_table.customerMobile, invoice_table.customerAddress, invoice_item_table.productName, invoice_item_table.qty, invoice_item_table.amount " +
            "FROM invoice_table " +
            "INNER JOIN invoice_item_table ON invoice_table.id = invoice_item_table.invoiceId " +
            "WHERE invoice_table.customerName = :customerName " +
            "ORDER BY invoice_table.date DESC")
    List<CustomerProductReport> getCustomerReport(String customerName);


    @Query("SELECT i.customerName, i.customerMobile, i.customerAddress, i.date, " +
            "item.productName, item.qty, item.amount " +
            "FROM invoice_table i " +
            "INNER JOIN invoice_item_table item ON i.id = item.invoiceId " +
            "WHERE i.customerName = :customerName " +
            "ORDER BY i.date DESC")
    List<CustomerProductReport> getCustomerFullReport(String customerName);


    @Query("SELECT DISTINCT customerName FROM invoice_table ORDER BY customerName ASC")
    List<String> getDistinctCustomerNames();

    @Query("SELECT DISTINCT customerName FROM invoice_table WHERE customerName LIKE :search || '%' ORDER BY customerName ASC")
    List<String> searchCustomerNames(String search);



    @Query("SELECT productName FROM product_table ORDER BY productName ASC")
    List<String> getAllProductNames();

}