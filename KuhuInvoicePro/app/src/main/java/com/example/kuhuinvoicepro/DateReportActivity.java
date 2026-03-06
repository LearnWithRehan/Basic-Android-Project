package com.example.kuhuinvoicepro;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DateReportActivity extends AppCompatActivity {

    EditText etFromDate, etToDate;
    Button btnSearch;
    RecyclerView recyclerReport;
    ImageView btnPdf;
    List<InvoiceWithItems> reportList = new ArrayList<>();
    InvoiceDao invoiceDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_report);

        etFromDate = findViewById(R.id.etFromDate);
        etToDate = findViewById(R.id.etToDate);
        btnSearch = findViewById(R.id.btnSearch);
        recyclerReport = findViewById(R.id.recyclerReport);
        btnPdf = findViewById(R.id.btnPdf);
        recyclerReport.setLayoutManager(new LinearLayoutManager(this));

        invoiceDao = AppDatabase.getInstance(this).invoiceDao();

        etFromDate.setOnClickListener(v -> showDatePicker(etFromDate));
        etToDate.setOnClickListener(v -> showDatePicker(etToDate));

        btnSearch.setOnClickListener(v -> loadReport());

        btnPdf.setOnClickListener(v -> {

            if (reportList.isEmpty()) {

                Toast.makeText(this,"No Data To Export",Toast.LENGTH_SHORT).show();

            } else {

                generatePdf();

            }

        });



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

//    private void loadReport() {
//
//        String fromDate = etFromDate.getText().toString();
//        String toDate = etToDate.getText().toString();
//
//        new Thread(() -> {
//
//            List<InvoiceWithItems> list =
//                    invoiceDao.getInvoicesByDate(fromDate, toDate);
//
//            runOnUiThread(() -> {
//
//                DateReportAdapter adapter =
//                        new DateReportAdapter(list);
//
//                recyclerReport.setAdapter(adapter);
//
//            });
//
//        }).start();
//    }

    private void loadReport() {

        String fromDate = etFromDate.getText().toString();
        String toDate = etToDate.getText().toString();

        new Thread(() -> {

            reportList = invoiceDao.getInvoicesByDate(fromDate, toDate);

            runOnUiThread(() -> {

                DateReportAdapter adapter = new DateReportAdapter(reportList);
                recyclerReport.setAdapter(adapter);

            });

        }).start();
    }

//    private void generatePdf() {
//
//        PdfDocument document = new PdfDocument();
//
//        Paint textPaint = new Paint();
//        Paint titlePaint = new Paint();
//        Paint headerPaint = new Paint();
//        Paint linePaint = new Paint();
//        Paint boxPaint = new Paint();
//
//        PdfDocument.PageInfo pageInfo =
//                new PdfDocument.PageInfo.Builder(1200, 2000, 1).create();
//
//        PdfDocument.Page page = document.startPage(pageInfo);
//        Canvas canvas = page.getCanvas();
//
//        // HEADER BACKGROUND
//        headerPaint.setColor(Color.parseColor("#2F80ED"));
//        canvas.drawRect(0,0,1200,120,headerPaint);
//
//        // TITLE
//        titlePaint.setColor(Color.WHITE);
//        titlePaint.setTextSize(45);
//        titlePaint.setFakeBoldText(true);
//
//        canvas.drawText("Date Wise Sales Report",350,80,titlePaint);
//
//        // LINE STYLE
//        linePaint.setColor(Color.GRAY);
//        linePaint.setStrokeWidth(2);
//
//        // TEXT STYLE
//        textPaint.setColor(Color.BLACK);
//        textPaint.setTextSize(28);
//
//        // BOX STYLE
//        boxPaint.setColor(Color.parseColor("#F2F6FF"));
//
//        int y = 180;
//
//        for (InvoiceWithItems data : reportList) {
//
//            // INVOICE BOX
//            canvas.drawRect(40,y-40,1160,y+160,boxPaint);
//
//            canvas.drawText("Invoice : " + data.invoice.invoiceNo, 60, y, textPaint);
//            y += 40;
//
//            canvas.drawText("Date : " + data.invoice.date, 60, y, textPaint);
//            y += 40;
//
//            canvas.drawText("Customer : " + data.invoice.customerName, 60, y, textPaint);
//            y += 40;
//
//            canvas.drawText("Mobile : " + data.invoice.customerMobile, 60, y, textPaint);
//            y += 40;
//
//            canvas.drawText("Net Amount : ₹" + data.invoice.netAmount, 60, y, textPaint);
//            y += 40;
//
//            canvas.drawText("Discount : ₹" + data.invoice.discount, 60, y, textPaint);
//            y += 40;
//
//            canvas.drawText("Total Amt : ₹" + data.invoice.totalAmount, 60, y, textPaint);
//            y += 60;
//
//            // PRODUCTS TITLE
//            textPaint.setFakeBoldText(true);
//            canvas.drawText("Products", 60, y, textPaint);
//            textPaint.setFakeBoldText(false);
//
//            y += 20;
//
//            canvas.drawLine(60,y,1140,y,linePaint);
//            y += 40;
//
//            for (InvoiceItemEntity item : data.items) {
//
//                canvas.drawText(item.productName, 80, y, textPaint);
//                canvas.drawText("Qty : " + item.qty, 600, y, textPaint);
//                canvas.drawText("₹ " + item.amount, 900, y, textPaint);
//
//                y += 40;
//            }
//
//            y += 30;
//
//            canvas.drawLine(60,y,1140,y,linePaint);
//
//            y += 80;
//        }
//
//        document.finishPage(page);
//
//        try {
//
//            File file = new File(getExternalFilesDir(null), "SalesReport.pdf");
//
//            FileOutputStream fos = new FileOutputStream(file);
//            document.writeTo(fos);
//            document.close();
//            fos.close();
//
//            openPdf(file);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }



    private void generatePdf() {

        PdfDocument document = new PdfDocument();

        Paint textPaint = new Paint();
        Paint boldPaint = new Paint();
        Paint titlePaint = new Paint();
        Paint headerPaint = new Paint();
        Paint linePaint = new Paint();
        Paint boxPaint = new Paint();
        Paint tableHeaderPaint = new Paint();
        Paint rowPaint = new Paint();

        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(1200, 2000, 1).create();

        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // HEADER
        headerPaint.setColor(Color.parseColor("#2F80ED"));
        canvas.drawRect(0,0,1200,120,headerPaint);

        titlePaint.setColor(Color.WHITE);
        titlePaint.setTextSize(45);
        titlePaint.setFakeBoldText(true);
        canvas.drawText("Date Wise Sales Report",350,80,titlePaint);

        // TEXT STYLE
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(28);

        boldPaint.setColor(Color.BLACK);
        boldPaint.setTextSize(30);
        boldPaint.setFakeBoldText(true);

        // LINE STYLE
        linePaint.setColor(Color.LTGRAY);
        linePaint.setStrokeWidth(2);

        // INVOICE BOX COLOR
        boxPaint.setColor(Color.parseColor("#F4F7FF"));

        // TABLE HEADER COLOR
        tableHeaderPaint.setColor(Color.parseColor("#E8F0FF"));

        int y = 180;

        for (InvoiceWithItems data : reportList) {

            int startY = y;

            // INVOICE BOX (only invoice details)
            int invoiceBoxHeight = 220;

            canvas.drawRoundRect(
                    30,
                    startY - 40,
                    1170,
                    startY + invoiceBoxHeight,
                    20,
                    20,
                    boxPaint
            );

            // INVOICE DETAILS
            canvas.drawText("Invoice : " + data.invoice.invoiceNo, 60, y, boldPaint);
            y += 40;

            canvas.drawText("Date : " + data.invoice.date, 60, y, textPaint);
            y += 40;

            canvas.drawText("Customer : " + data.invoice.customerName, 60, y, textPaint);
            y += 40;

            canvas.drawText("Mobile : " + data.invoice.customerMobile, 60, y, textPaint);
            y += 40;

            canvas.drawText("Total : ₹" + data.invoice.totalAmount, 60, y, textPaint);
            y += 40;

            canvas.drawText("Discount : ₹" + data.invoice.discount, 60, y, textPaint);
            y += 40;

            canvas.drawText("Net Amount : ₹" + data.invoice.netAmount, 60, y, boldPaint);
            y += 60;


            // TABLE HEADER BACKGROUND
            canvas.drawRect(40, y - 30, 1160, y + 10, tableHeaderPaint);

            canvas.drawText("Product", 80, y, boldPaint);
            canvas.drawText("Qty", 650, y, boldPaint);
            canvas.drawText("Amount", 900, y, boldPaint);

            y += 20;
            canvas.drawLine(50,y,1150,y,linePaint);
            y += 40;

            int row = 0;

            // PRODUCTS
            for (InvoiceItemEntity item : data.items) {

                // alternate row color
                if(row % 2 == 0){

                    rowPaint.setColor(Color.parseColor("#F9FAFF"));
                    canvas.drawRect(40, y - 30, 1160, y + 10, rowPaint);

                }

                canvas.drawText(item.productName,80,y,textPaint);

                // Qty with KG
                canvas.drawText(item.qty + "kg",650,y,textPaint);

                canvas.drawText("₹ "+item.amount,900,y,textPaint);

                y += 40;
                row++;
            }

            y += 60;
        }

        document.finishPage(page);

        try {

            File file = new File(getExternalFilesDir(null), "SalesReport.pdf");

            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);

            document.close();
            fos.close();

            openPdf(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openPdf(File file) {

        Uri uri = FileProvider.getUriForFile(
                this,
                getPackageName() + ".provider",
                file
        );

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(intent, "Open PDF"));
    }

}