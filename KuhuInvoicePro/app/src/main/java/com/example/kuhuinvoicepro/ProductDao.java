package com.example.kuhuinvoicepro;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    void insertProduct(ProductEntity product);

    @Query("SELECT * FROM product_table ORDER BY productName ASC")
    List<ProductEntity> getAllProducts();

    @Query("DELETE FROM product_table WHERE id = :id")
    void deleteProduct(int id);


    @Query("SELECT productName FROM product_table ORDER BY productName ASC")
    List<String> getAllProductNames();





}