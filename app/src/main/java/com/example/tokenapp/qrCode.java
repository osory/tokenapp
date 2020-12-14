package com.example.tokenapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Objects;

public class qrCode extends AppCompatActivity {

    ImageView qrCode;
    Button back, genQr, scanQr;
    TextView token;
    String userId;
    FirebaseAuth fAuth;
    FirebaseUser user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        back = findViewById(R.id.backQrcode);
        genQr = findViewById(R.id.genQrcodeButton);
        scanQr = findViewById(R.id.scanQrcodeButton);
        qrCode = findViewById(R.id.qrCodeImageView);
        token = findViewById(R.id.profileToken2);

        fAuth = FirebaseAuth.getInstance();
        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        user = fAuth.getCurrentUser();


        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        final DocumentReference ref = fStore.collection("users").document(userId);


        genQr.setOnClickListener(v -> ref.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String user = documentSnapshot.getString("UID");
                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        BitMatrix bitMatrix;
                        try {
                            bitMatrix = multiFormatWriter.encode(user, BarcodeFormat.QR_CODE, 500, 500);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                            qrCode.setImageBitmap(bitmap);
                        } catch (WriterException writerException) {
                            writerException.printStackTrace();
                        }
                    } else {
                        Toast.makeText(qrCode.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()));

        scanQr.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),Scanner.class)));

        back.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),MainActivity.class)));


    }
}
