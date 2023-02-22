package com.example.mobileprogramming_assignment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CertActivity extends AppCompatActivity {

    Button btnBackHome, btnDownload;
    TextView txtUsername;
    String userID, name;

    // creating a bitmap variable for storing our images
    Bitmap bmp, scaledBmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cert);

        userID = getIntent().getStringExtra("userID");
        name = getIntent().getStringExtra("name");

        btnBackHome = findViewById(R.id.btnBackHome);
        btnBackHome.setOnClickListener(view -> {
            Intent intent = new Intent(CertActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        txtUsername = findViewById(R.id.txtUsername);
        txtUsername.setText(name);

        btnDownload = findViewById(R.id.btnDownload);

        btnDownload.setOnClickListener(v -> {
            // calling method to generate our PDF file.
            generatePDF();
        });
    }

    private void generatePDF() {

        int pageHeight = 1120;
        int pageWidth = 792;

        PdfDocument document = new PdfDocument();
        Paint paint = new Paint();
        Paint title = new Paint();

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.certificate);
        scaledBmp = Bitmap.createScaledBitmap(bmp, pageWidth, pageHeight, false);

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        canvas.drawBitmap(scaledBmp, 0, 0, paint);

        // below line is used for setting our text to center of PDF.
        title.setTypeface(ResourcesCompat.getFont(this, R.font.alex_brush));
        title.setColor(ContextCompat.getColor(this, R.color.black));
        title.setTextAlign(Paint.Align.CENTER);
        title.setTextSize(65);

        // Draw content on the canvas using standard Android drawing methods
        canvas.drawText(name, 396, 600, title);

        File file = new File(Environment.getExternalStorageDirectory(), "Cert.pdf");
        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }

            document.finishPage(page);
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(CertActivity.this, "Certificate generated successfully.", Toast.LENGTH_SHORT).show();
            document.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
