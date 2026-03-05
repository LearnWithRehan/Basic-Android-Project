package com.example.kuhuinvoicepro;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {InvoiceEntity.class, InvoiceItemEntity.class},
        version = 2
)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract InvoiceDao invoiceDao();

    public static synchronized AppDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "invoice_database"
                    ).allowMainThreadQueries()   // ⚠ temporary only
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}