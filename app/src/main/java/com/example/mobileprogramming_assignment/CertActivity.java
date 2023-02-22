package com.example.mobileprogramming_assignment;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Objects;

public class CertActivity extends AppCompatActivity {

    Button btnBackHome, btnDownload;
    TextView txtUsername;
    String userID, name;

    // declaring width and height for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;

    // creating a bitmap variable for storing our images
    Bitmap bmp, scaledbmp;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cert);

        userID = getIntent().getStringExtra("userID");
        name = getIntent().getStringExtra("name");

        btnBackHome = findViewById(R.id.btnBackHome);
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CertActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        txtUsername = findViewById(R.id.txtUsername);
        txtUsername.setText(name);

        btnDownload = findViewById(R.id.btnDownload);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling method to generate our PDF file.
                generatePDF();
            }
        });
    }

    private void generatePDF() {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1120, 792, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint title = new Paint();

        // Draw content on the canvas using standard Android drawing methods
        canvas.drawText("Hello, World!", 560, 380, title);

        File file = new File(Environment.getExternalStorageDirectory(), "Cert.pdf");
        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }

            document.finishPage(page);
            document.writeTo(new FileOutputStream(file));
            document.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
