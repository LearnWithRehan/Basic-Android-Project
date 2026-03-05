package com.example.kuhuinvoicepro;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class DateReportActivity extends AppCompatActivity {

    EditText etFromDate, etToDate;
    Button btnSearch;
    RecyclerView recyclerReport;

    InvoiceDao invoiceDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_report);

        etFromDate = findViewById(R.id.etFromDate);
        etToDate = findViewById(R.id.etToDate);
        btnSearch = findViewById(R.id.btnSearch);
        recyclerReport = findViewById(R.id.recyclerReport);

        recyclerReport.setLayoutManager(new LinearLayoutManager(this));

        invoiceDao = AppDatabase.getInstance(this).invoiceDao();

        etFromDate.setOnClickListener(v -> showDatePicker(etFromDate));
        etToDate.setOnClickListener(v -> showDatePicker(etToDate));

        btnSearch.setOnClickListener(v -> loadReport());
    }

    private void showDatePicker(EditText editText) {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {

                    month = month + 1;

                    String date = year + "-" +
                            String.format("%02d", month) + "-" +
                            String.format("%02d", dayOfMonth);

                    editText.setText(date);

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    private void loadReport() {

        String fromDate = etFromDate.getText().toString();
        String toDate = etToDate.getText().toString();

        new Thread(() -> {

            List<InvoiceWithItems> list =
                    invoiceDao.getInvoicesByDate(fromDate, toDate);

            runOnUiThread(() -> {

                DateReportAdapter adapter =
                        new DateReportAdapter(list);

                recyclerReport.setAdapter(adapter);

            });

        }).start();
    }
}