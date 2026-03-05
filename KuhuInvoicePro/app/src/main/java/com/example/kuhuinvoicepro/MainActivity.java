package com.example.kuhuinvoicepro;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    AppDatabase database;
    InvoiceDao invoiceDao;
    Spinner spProduct;
    EditText etQty, etRate;
    EditText etDiscount;
    TextView tvNetAmount;
    Button btnAddItem,btnGeneratePdf,btnClear ;
    RecyclerView recyclerItems;
    EditText etCustomerName, etCustomerAddress, etCustomerMobile;

    TextView tvTotalQty, tvTotalRate, tvTotalAmount, tvTotalProduct;

    List<InvoiceItem> itemList;
  public   InvoiceAdapter adapter;

    int srNo = 1;
    double totalAmount = 0;
    double totalQty = 0;
    double totalRate = 0;
    int lastSavedItemIndex = 0;
    String invoiceNo = null;
    int savedInvoiceId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spProduct = findViewById(R.id.spProduct);
        etQty = findViewById(R.id.etQty);
        etRate = findViewById(R.id.etRate);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnClear = findViewById(R.id.btnClear);
        recyclerItems = findViewById(R.id.recyclerItems);

        tvTotalQty = findViewById(R.id.tvTotalQty);
      //  tvTotalRate = findViewById(R.id.tvTotalRate);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvTotalProduct = findViewById(R.id.tvTotalProduct);
        etDiscount = findViewById(R.id.etDiscount);
        tvNetAmount = findViewById(R.id.tvNetAmount);
        btnGeneratePdf = findViewById(R.id.btnGeneratePdf);
        etCustomerName = findViewById(R.id.etCustomerName);
        etCustomerAddress = findViewById(R.id.etCustomerAddress);
        etCustomerMobile = findViewById(R.id.etCustomerMobile);

        database = AppDatabase.getInstance(this);
        invoiceDao = database.invoiceDao();
        itemList = new ArrayList<>();
        adapter = new InvoiceAdapter(itemList, position -> {
            showDeleteDialog(position);
        });
        recyclerItems.setAdapter(adapter);

        recyclerItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerItems.setAdapter(adapter);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.product_list,
                R.layout.spinner_item
        );

        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spProduct.setAdapter(spinnerAdapter);

        // IMPORTANT SETTINGS
        recyclerItems.setNestedScrollingEnabled(false);
        recyclerItems.setHasFixedSize(false);
        recyclerItems.setItemAnimator(null);

        btnAddItem.setOnClickListener(v -> addItem());

        btnClear.setOnClickListener(v -> {

            etCustomerName.setText("");
            etCustomerAddress.setText("");
            etCustomerMobile.setText("");
            etQty.setText("");
            etRate.setText("");
            etDiscount.setText("");
            spProduct.setSelection(0);

            adapter.clearItems();

            srNo = 1;
            totalAmount = 0;
            totalQty = 0;
            totalRate = 0;
            invoiceNo = null;
            savedInvoiceId = -1;
            lastSavedItemIndex = 0;
            tvTotalProduct.setText("Items: 0");
            tvTotalQty.setText("0");
            tvTotalRate.setText("0");
            tvTotalAmount.setText("0.00");
            tvNetAmount.setText("₹ 0.00");

            // 🔥 SET FOCUS BACK TO CUSTOMER NAME
            etCustomerName.requestFocus();

            // 🔥 OPEN KEYBOARD
            etCustomerName.postDelayed(() -> {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(etCustomerName, InputMethodManager.SHOW_IMPLICIT);
                }
            }, 200);

        });



        etDiscount.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateNetAmount();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });



        btnGeneratePdf.setOnClickListener(v -> generatePDF());



    }


//    private void showDeleteDialog(int position) {
//
//        InvoiceItem item = itemList.get(position);
//
//        new androidx.appcompat.app.AlertDialog.Builder(this)
//                .setTitle("Delete Item")
//                .setMessage("Are you sure you want to delete \""
//                        + item.getProductName() + "\" ?")
//                .setPositiveButton("Yes", (dialog, which) -> {
//
//                    totalAmount -= item.getAmount();
//                    totalQty -= item.getQty();
//                    totalRate -= item.getRate();
//
//                    adapter.removeItem(position);
//
//                    updateTotals();
//
//                    Toast.makeText(this, "Item Deleted", Toast.LENGTH_SHORT).show();
//                })
//                .setNegativeButton("No", null)
//                .setCancelable(false)
//                .show();
//    }


//    private void showDeleteDialog(int position) {
//
//        InvoiceItem item = itemList.get(position);
//
//        new androidx.appcompat.app.AlertDialog.Builder(this)
//                .setTitle("आइटम हटाएँ")
//                .setMessage("क्या आप \""
//                        + item.getProductName() +
//                        "\" को हटाना चाहते हैं?")
//                .setPositiveButton("हाँ", (dialog, which) -> {
//
//                    totalAmount -= item.getAmount();
//                    totalQty -= item.getQty();
//                    totalRate -= item.getRate();
//
//                    adapter.removeItem(position);
//
//                    updateTotals();
//
//                    Toast.makeText(this, "आइटम सफलतापूर्वक हटाया गया", Toast.LENGTH_SHORT).show();
//                })
//                .setNegativeButton("नहीं", null)
//                .setCancelable(false)
//                .show();
//    }




    private void showDeleteDialog(int position) {

        InvoiceItem item = itemList.get(position);

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("आइटम हटाएँ")
                .setMessage("क्या आप \"" + item.getProductName() + "\" को हटाना चाहते हैं?")
                .setPositiveButton("हाँ", (dialog, which) -> {

                    totalAmount -= item.getAmount();
                    totalQty -= item.getQty();
                    totalRate -= item.getRate();

                    new Thread(() -> {

                        if(item.getItemId() != 0){
                            invoiceDao.deleteItemById(item.getItemId());
                        }

                    }).start();

                    adapter.removeItem(position);

                    updateTotals();

                    Toast.makeText(this,
                            "आइटम सफलतापूर्वक हटाया गया",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("नहीं", null)
                .setCancelable(false)
                .show();
    }


    private void updateTotals() {

        tvTotalProduct.setText("Items: " + itemList.size());
        tvTotalQty.setText(String.valueOf(totalQty));
     //   tvTotalRate.setText(String.valueOf(totalRate));
        tvTotalAmount.setText(String.format("%.2f", totalAmount));

        updateNetAmount();
    }

    private void addItem() {

        String product = spProduct.getSelectedItem().toString();
        String qtyStr = etQty.getText().toString().trim();
        String rateStr = etRate.getText().toString().trim();

        // ✅ PRODUCT VALIDATION (IMPORTANT)
        if (product.equals("प्रोडक्ट चुनें")) {
            Toast.makeText(this,
                    "कृपया प्रोडक्ट चुनें ",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ EMPTY FIELD VALIDATION
        if (qtyStr.isEmpty() || rateStr.isEmpty()) {
            Toast.makeText(this,
                    "Please enter Quantity and Amount",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double qty = Double.parseDouble(qtyStr);
        double rate = Double.parseDouble(rateStr);

        InvoiceItem item = new InvoiceItem(0, srNo, product, qty, rate);
        itemList.add(item);

        // ✅ Update totals
        totalQty += qty;
        totalRate += rate;
        totalAmount += item.getAmount();

        tvTotalQty.setText(String.valueOf(totalQty)+"kg");
       // tvTotalRate.setText(String.valueOf(totalRate));
        tvTotalAmount.setText(String.format("%.2f", totalAmount));
        tvTotalProduct.setText("Items: " + itemList.size());

        adapter.notifyItemInserted(itemList.size() - 1);

        recyclerItems.post(() -> recyclerItems.requestLayout());

        srNo++;

        spProduct.setSelection(0);
        etQty.setText("");
        etRate.setText("");


        // 🔥 VERY IMPORTANT LINE
        updateNetAmount();

    }



    private void updateNetAmount() {
        String discountStr = etDiscount.getText().toString().trim();

        double discount = discountStr.isEmpty() ? 0 : Double.parseDouble(discountStr);
        double netAmount = totalAmount - discount;

        tvNetAmount.setText("₹ " + String.format("%.2f", netAmount));
    }










    // ================= NUMBER TO WORDS =================
    private String convertToWords(int number) {

        String[] units = {"", "One ", "Two ", "Three ", "Four ", "Five ",
                "Six ", "Seven ", "Eight ", "Nine ", "Ten ", "Eleven ",
                "Twelve ", "Thirteen ", "Fourteen ", "Fifteen ",
                "Sixteen ", "Seventeen ", "Eighteen ", "Nineteen "};

        String[] tens = {"", "", "Twenty ", "Thirty ", "Forty ",
                "Fifty ", "Sixty ", "Seventy ", "Eighty ", "Ninety "};

        if (number < 20)
            return units[number];

        if (number < 100)
            return tens[number / 10] + units[number % 10];

        if (number < 1000)
            return units[number / 100] + "Hundred " +
                    convertToWords(number % 100);

        if (number < 100000)
            return convertToWords(number / 1000) + "Thousand " +
                    convertToWords(number % 1000);

        if (number < 10000000)
            return convertToWords(number / 100000) + "Lakh " +
                    convertToWords(number % 100000);

        return "Amount Too Large";
    }















    private void generatePDF() {

//        if (invoiceNo == null) {
//
//            invoiceNo = "INV-" +
//                    new SimpleDateFormat("yyyyMMddHHmmss",
//                            Locale.getDefault()).format(new Date());
//        }

        String name = etCustomerName.getText().toString();
        String address = etCustomerAddress.getText().toString();
        String number = etCustomerMobile.getText().toString();

        if (name.isEmpty()) {
            etCustomerName.setError("Please enter customer name");
            etCustomerName.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            etCustomerAddress.setError("Please enter customer address");
            etCustomerAddress.requestFocus();
            return;
        }

        if (number.isEmpty()) {
            etCustomerMobile.setError("Please enter mobile number");
            etCustomerMobile.requestFocus();
            return;
        }

        if (number.length() != 10) {
            etCustomerMobile.setError("Enter valid 10 digit mobile number");
            etCustomerMobile.requestFocus();
            return;
        }

        PdfDocument pdfDocument = new PdfDocument();

        //int pageWidth = 1200;
      //  int pageHeight = 3000;

        int pageWidth = 1200;

// 🔥 Calculate Dynamic Height
        int headerHeight = 300;
        int customerHeight = 200;
        int tableHeaderHeight = 80;
        int itemHeight = 60;
        int totalSectionHeight = 700;

// 👇 Items ke hisaab se height calculate
        int itemsHeight = itemList.size() * itemHeight;

// 👇 Final page height
        int pageHeight = headerHeight
                + customerHeight
                + tableHeaderHeight
                + itemsHeight
                + totalSectionHeight
                + 200;  // extra margin safety

        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();

        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        int y;

        // ================= DATE & INVOICE NO =================
//        String currentDate = new SimpleDateFormat("dd MMM yyyy, hh:mm a",
//                Locale.getDefault()).format(new Date());

        String currentDate = new SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
        ).format(new Date());


//        String invoiceNo = "INV-" +
//                new SimpleDateFormat("yyyyMMddHHmmss",
//                        Locale.getDefault()).format(new Date());

        if (invoiceNo == null) {

            invoiceNo = "INV-" +
                    new SimpleDateFormat("yyyyMMddHHmmss",
                            Locale.getDefault()).format(new Date());
        }


        // ================= CALCULATE TOTALS =================
        double totalQty = 0;
        double totalRate = 0;
        double totalAmount = 0;

        for (InvoiceItem item : itemList) {
            totalQty += item.getQty();
            totalRate += item.getRate();
            totalAmount += item.getAmount();
        }

        // ================= HEADER =================
        paint.setColor(Color.parseColor("#F57C00"));
        canvas.drawRect(0, 0, pageWidth, 220, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(36);
        paint.setFakeBoldText(true);
        canvas.drawText("Kuhu The Annapurna Multigrain's & Spice's", 220, 80, paint);

        paint.setTextSize(24);
        paint.setFakeBoldText(false);
        canvas.drawText("331, Vill Sabli Hapur Uttar Pradesh 245101", 220, 120, paint);
        canvas.drawText("9412121383", 220, 155, paint);
        canvas.drawText("anujtyagihpr@gmail.com", 220, 185, paint);

        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        Bitmap scaledLogo = Bitmap.createScaledBitmap(logo,150,150,false);
        canvas.drawBitmap(scaledLogo, 40, 35, null);

        y = 300;

        // ================= TITLE =================
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.parseColor("#F57C00"));
        paint.setTextSize(45);
        paint.setFakeBoldText(true);
        canvas.drawText("INVOICE", pageWidth / 2, y, paint);

        // ================= DATE & INVOICE NUMBER =================
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(26);
        paint.setColor(Color.BLACK);
        paint.setFakeBoldText(true);

        canvas.drawText("Invoice No: " + invoiceNo, pageWidth - 50, y - 30, paint);
        canvas.drawText("Date: " + currentDate, pageWidth - 50, y + 10, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        y += 80;

        // ================= CUSTOMER =================
        paint.setTextSize(28);
        paint.setFakeBoldText(true);
        canvas.drawText("Bill To:", 50, y, paint);

        paint.setFakeBoldText(false);
        y += 40;
        canvas.drawText(etCustomerName.getText().toString(), 50, y, paint);
        y += 35;
        canvas.drawText(etCustomerAddress.getText().toString(), 50, y, paint);
        y += 35;
        canvas.drawText(etCustomerMobile.getText().toString(), 50, y, paint);

        y += 60;

        // ================= TABLE HEADER =================
        int startX = 50;
        int endX = pageWidth - 50;

        paint.setColor(Color.parseColor("#F57C00"));
        canvas.drawRect(startX, y, endX, y + 60, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(26);
        paint.setFakeBoldText(true);

        canvas.drawText("Sr", 70, y + 40, paint);
        canvas.drawText("Product", 150, y + 40, paint);
        canvas.drawText("Qty", 600, y + 40, paint);
       // canvas.drawText("Rate", 800, y + 40, paint);
        canvas.drawText("Amount", 1000, y + 40, paint);

        y += 60;



        // ================= TABLE ITEMS =================
        paint.setColor(Color.BLACK);
        paint.setTextSize(24);
        paint.setFakeBoldText(false);

        for (InvoiceItem item : itemList) {

            canvas.drawLine(startX, y, endX, y, paint);

            // LEFT columns
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(String.valueOf(item.getSrNo()), 70, y + 35, paint);
            canvas.drawText(item.getProductName(), 150, y + 35, paint);

            // RIGHT columns
            paint.setTextAlign(Paint.Align.RIGHT);

            canvas.drawText(
                    String.format("%.2f Kg", item.getQty()),
                    650,                 // shifted a little left
                    y + 35,
                    paint
            );

//            canvas.drawText(
//                    String.format("%.2f", item.getRate()),
//                    850,
//                    y + 35,
//                    paint
//            );

            canvas.drawText(
                    "₹ " + String.format("%.2f", item.getAmount()),
                    1100,                // last column end
                    y + 35,
                    paint
            );

            y += 60;
        }





        canvas.drawLine(startX, y, endX, y, paint);
        y += 60;

// ================= TOTAL ROW =================
        paint.setColor(Color.parseColor("#FFF3E0"));
        canvas.drawRect(startX, y, endX, y + 60, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(24);
        paint.setFakeBoldText(true);

// LEFT aligned columns
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Total", 70, y + 40, paint);
        canvas.drawText("Items: " + itemList.size(), 150, y + 40, paint);

// RIGHT aligned numeric columns
        paint.setTextAlign(Paint.Align.RIGHT);

        canvas.drawText(
                String.format("%.2f Kg", totalQty),
                650,
                y + 40,
                paint
        );

//        canvas.drawText(
//                String.format("%.2f", totalRate),
//                850,
//                y + 40,
//                paint
//        );

        canvas.drawText(
                "₹ " + String.format("%.2f", totalAmount),
                1100,
                y + 40,
                paint
        );

        y += 100;


        // ================= DISCOUNT & NET =================
        double discount = 0;
        try {
            discount = Double.parseDouble(etDiscount.getText().toString());
        } catch (Exception e) {
            discount = 0;
        }

        double net = totalAmount - discount;

        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(28);
        paint.setColor(Color.BLACK);

        canvas.drawText(
                "Total Amount: ₹ " + String.format("%.2f", totalAmount),
                pageWidth - 50,
                y,
                paint
        );


        y += 40;
        canvas.drawText(
                "(-)Discount: - ₹ " + String.format("%.2f", discount),
                pageWidth - 50,
                y,
                paint
        );

        y += 40;
        paint.setColor(Color.parseColor("#F57C00"));
        paint.setFakeBoldText(true);

        canvas.drawText(
                "Net Amount: ₹ " + String.format("%.2f", net),
                pageWidth - 50,
                y,
                paint
        );





        // ===== SAVE IN ROOM DATABASE =====
// ===== SAVE IN ROOM DATABASE (BACKGROUND THREAD) =====
// ===== SAVE IN ROOM DATABASE (NO LAMBDA VERSION) =====
//
//// Make local final copies
//        final String fName = name;
//        final String fAddress = address;
//        final String fNumber = number;
//        final double fTotalAmount = totalAmount;
//        final double fDiscount = discount;
//        final double fNet = net;
//        final String fDate = currentDate;
//
//// Create final copy of item list
//        final List<InvoiceItem> finalItemList = new ArrayList<>(itemList);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                // 1️⃣ Insert Invoice
//                InvoiceEntity invoice = new InvoiceEntity();
//                invoice.customerName = fName;
//                invoice.customerAddress = fAddress;
//                invoice.customerMobile = fNumber;
//                invoice.totalAmount = fTotalAmount;
//                invoice.discount = fDiscount;
//                invoice.netAmount = fNet;
//                invoice.date = fDate;
//
//                long invoiceId = invoiceDao.insertInvoice(invoice);
//
//                // 2️⃣ Insert All Items
//                List<InvoiceItemEntity> itemEntities = new ArrayList<>();
//
////                for (InvoiceItem item : finalItemList) {
////
////                    InvoiceItemEntity entity = new InvoiceItemEntity();
////                    entity.invoiceId = (int) invoiceId;
////                    entity.productName = item.getProductName();
////                    entity.qty = item.getQty();
////                    entity.rate = item.getRate();
////                    entity.amount = item.getAmount();
////
////                    itemEntities.add(entity);
////                }
//
//                for (int i = lastSavedItemIndex; i < finalItemList.size(); i++) {
//
//                    InvoiceItem item = finalItemList.get(i);
//
//                    InvoiceItemEntity entity = new InvoiceItemEntity();
//                    entity.invoiceId = (int) invoiceId;
//                    entity.productName = item.getProductName();
//                    entity.qty = item.getQty();
//                    entity.rate = item.getRate();
//                    entity.amount = item.getAmount();
//
//                    itemEntities.add(entity);
//                }
//
//                invoiceDao.insertInvoiceItems(itemEntities);
//                lastSavedItemIndex = finalItemList.size();
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(MainActivity.this,
//                                "Invoice + Products Saved Successfully",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }).start();



        // Make local final copies
        final String fName = name;
        final String fAddress = address;
        final String fNumber = number;
        final double fTotalAmount = totalAmount;
        final double fDiscount = discount;
        final double fNet = net;
        final String fDate = currentDate;
        final String fInvoiceNo = invoiceNo;

// Create final copy of item list
        final List<InvoiceItem> finalItemList = new ArrayList<>(itemList);

        new Thread(new Runnable() {
            @Override
            public void run() {

                long invoiceId;

                // 1️⃣ FIRST TIME INSERT
                if (savedInvoiceId == -1) {

                    InvoiceEntity invoice = new InvoiceEntity();
                    invoice.invoiceNo = fInvoiceNo;
                    invoice.customerName = fName;
                    invoice.customerAddress = fAddress;
                    invoice.customerMobile = fNumber;
                    invoice.totalAmount = fTotalAmount;
                    invoice.discount = fDiscount;
                    invoice.netAmount = fNet;
                    invoice.date = fDate;

                    invoiceId = invoiceDao.insertInvoice(invoice);

                    savedInvoiceId = (int) invoiceId;

                }
                else {

                    // 2️⃣ UPDATE TOTALS
                    invoiceDao.updateInvoiceTotals(
                            savedInvoiceId,
                            fTotalAmount,
                            fDiscount,
                            fNet
                    );

                    invoiceId = savedInvoiceId;
                }

                // 3️⃣ Insert ONLY NEW ITEMS
                List<InvoiceItemEntity> itemEntities = new ArrayList<>();

                for (int i = lastSavedItemIndex; i < finalItemList.size(); i++) {

                    InvoiceItem item = finalItemList.get(i);

                    InvoiceItemEntity entity = new InvoiceItemEntity();
                    entity.invoiceId = (int) invoiceId;
                    entity.productName = item.getProductName();
                    entity.qty = item.getQty();
                    entity.rate = item.getRate();
                    entity.amount = item.getAmount();

                    long itemId = invoiceDao.insertInvoiceItem(entity);

                    // 👇 RecyclerView object me database id set karo
                    item.setItemId((int) itemId);
                }

                lastSavedItemIndex = finalItemList.size();

                lastSavedItemIndex = finalItemList.size();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,
                                "Invoice Updated Successfully",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();



// ================= LEFT SIDE OUTSTANDING & AMOUNT IN WORDS =================







        // ================= NET + LEFT SECTION FIX =================

// Right side already Net Amount pe hai
        int rightStartY = y;

// Left side bhi same Y se start hoga
        int leftStartY = rightStartY;

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        paint.setTextSize(26);
        paint.setFakeBoldText(true);

        int leftX = 50;

// 1️⃣ Tagline
        canvas.drawText(
                "मल्टीग्रेन और मसालों के स्वाद से हर खाने को खास बनाएं",
                leftX,
                leftStartY,
                paint
        );

        leftStartY += 40;

// 2️⃣ Outstanding Payment
        canvas.drawText(
                "Total Outstanding Payment : ₹ " + String.format("%.2f", net),
                leftX,
                leftStartY,
                paint
        );

        leftStartY += 40;

// 3️⃣ Amount in Words
        paint.setFakeBoldText(false);
        paint.setTextSize(24);

        String amountInWords = convertToWords((int) net);

        canvas.drawText(
                "Amount In Words : " + amountInWords + " Rupees Only",
                leftX,
                leftStartY,
                paint
        );

// IMPORTANT 👉 y ko update karo max height ke hisaab se
        y = Math.max(leftStartY, rightStartY) + 80;

        // ================= QR =================
        Bitmap qr = BitmapFactory.decodeResource(getResources(), R.drawable.qr);
        Bitmap scaledQR = Bitmap.createScaledBitmap(qr,250,250,false);
        canvas.drawBitmap(scaledQR, 450, y, null);

        y += 280;


        // ================= THANK YOU FOOTER =================
//        paint.setColor(Color.LTGRAY);
//        canvas.drawLine(200, y - 120, pageWidth - 200, y - 120, paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.parseColor("#4CAF50"));
        paint.setTextSize(30);
        paint.setFakeBoldText(true);

        canvas.drawText(
                "Thank You For Shopping With Us!",
                pageWidth / 2,
                y,
                paint
        );

        y += 40;

        paint.setFakeBoldText(false);
        paint.setTextSize(24);
        paint.setColor(Color.DKGRAY);

        canvas.drawText(
                "फिर से सेवा का अवसर दें 🙏",
                pageWidth / 2,
                y,
                paint
        );

        y += 35;

        canvas.drawText(
                "घर बैठे ताज़ा राशन मंगाने के लिए 📞 9412121383 पर संपर्क करें",
                pageWidth / 2,
                y,
                paint
        );

        y += 60;


        // ================= SIGNATURE =================

        Bitmap sign = BitmapFactory.decodeResource(getResources(), R.drawable.sign);

        if (sign != null) {

            Bitmap scaledSign = Bitmap.createScaledBitmap(sign, 200, 120, false);

            int signatureY = pageHeight - 300;   // always bottom safe

            canvas.drawBitmap(scaledSign, 900, signatureY, null);

            paint.setColor(Color.BLACK);
            paint.setTextSize(22);
            canvas.drawText("Authorized Signature", 1000, signatureY + 150, paint);
        }

        pdfDocument.finishPage(page);

        File file = new File(getExternalFilesDir(null),
                "Invoice_" + invoiceNo + ".pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            pdfDocument.close();

            Uri uri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider",
                    file
            );

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}