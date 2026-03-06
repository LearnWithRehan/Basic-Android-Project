package com.example.kuhuinvoicepro;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    EditText etProductName;
    Button btnSave;

    RecyclerView recyclerProducts;

    ProductDao productDao;

    List<ProductEntity> productList = new ArrayList<>();
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etProductName = findViewById(R.id.etProductName);
        btnSave = findViewById(R.id.btnSaveProduct);
        recyclerProducts = findViewById(R.id.recyclerProducts);

        productDao = AppDatabase.getInstance(this).productDao();

        adapter = new ProductAdapter(productList, product -> showDeleteDialog(product));

        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));
        recyclerProducts.setAdapter(adapter);

        loadProducts();

        btnSave.setOnClickListener(v -> {

            String name = etProductName.getText().toString().trim();

            if(name.isEmpty()){
                Toast.makeText(this,"Enter Product Name",Toast.LENGTH_SHORT).show();
                return;
            }

            ProductEntity product = new ProductEntity();
            product.productName = name;

            productDao.insertProduct(product);

            etProductName.setText("");

            Toast.makeText(this,"Product Added",Toast.LENGTH_SHORT).show();

            loadProducts();

        });

    }

    private void loadProducts(){

        productList.clear();
        productList.addAll(productDao.getAllProducts());
        adapter.notifyDataSetChanged();

    }

    private void showDeleteDialog(ProductEntity product){

        new AlertDialog.Builder(this)
                .setTitle("Delete Product")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes",(dialog,which)->{

                    productDao.deleteProduct(product.id);

                    loadProducts();

                    Toast.makeText(this,"Deleted",Toast.LENGTH_SHORT).show();

                })
                .setNegativeButton("No",null)
                .show();

    }

}