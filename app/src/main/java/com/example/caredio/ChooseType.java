package com.example.caredio;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChooseType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_type);
        String patientId = getIntent().getStringExtra("PATIENT_ID");

        CardView cardView = findViewById(R.id.cardView14);
        CardView cardView1 = findViewById(R.id.cardView15);
        CardView cardView2 = findViewById(R.id.cardView16);
        CardView cardView3 = findViewById(R.id.cardView17);// Replace with your CardView ID

        cardView.setOnClickListener(v -> {
            // Handle click event
            Intent intent = new Intent(ChooseType.this, HeartDiagnoDoc.class);
            intent.putExtra("PATIENT_ID", patientId); // Pass patient ID again
            startActivity(intent);
        });
        cardView1.setOnClickListener(v -> {
            // Handle click event
            Intent intent = new Intent(ChooseType.this, BloodSuDiagnDoc.class);
            intent.putExtra("PATIENT_ID", patientId); // Pass patient ID again
            startActivity(intent);
        });
        cardView2.setOnClickListener(v -> {
            // Handle click event
            Intent intent = new Intent(ChooseType.this, PoidsDiagnoDoc.class);
            intent.putExtra("PATIENT_ID", patientId); // Pass patient ID again
            startActivity(intent);
        });
        cardView3.setOnClickListener(v -> {
            // Handle click event
            Intent intent = new Intent(ChooseType.this, BloodPruDoc.class);
            intent.putExtra("PATIENT_ID", patientId); // Pass patient ID again
            startActivity(intent);
        });




    }
}