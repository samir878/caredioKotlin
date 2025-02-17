package com.example.caredio;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class BloodSuDiagnDoc extends AppCompatActivity {

    private FirebaseFirestore db;
    private LinearLayout historyContainer;
    private static final String TAG = "BloodSuDiagno";
    private String patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_su_diagn_doc);

        historyContainer = findViewById(R.id.historyContainer);
        db = FirebaseFirestore.getInstance();

        // Get the current logged-in user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        patientId = getIntent().getStringExtra("PATIENT_ID");


        // Fetch blood pressure data based on the patient ID
        fetchBloodPressureData();


    }

    private void fetchBloodPressureData() {
        if (patientId == null) {
            Toast.makeText(this, "No user is logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch blood pressure records for the patient
        db.collection("Patients")
                .document(patientId)
                .collection("Bloodsu")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(this, "No Blood Sugar records found!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Loop through each blood pressure record
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Long bloodsuValue = document.getLong("blood sugar");


                        if (bloodsuValue != null ) {
                            int bloodsu = bloodsuValue.intValue();

                            generateHistoryCard(bloodsu);
                        } else {
                            Log.e(TAG, "Invalid data format in document: " + document.getId());
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to connect to the database!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error fetching data", e);
                });
    }

    private void generateHistoryCard(int bloodsu) {
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 8, 0, 8);
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(16);
        cardView.setCardElevation(6);
        cardView.setPadding(24, 16, 24, 16);
        cardView.setCardBackgroundColor(Color.WHITE);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        TextView bpText = new TextView(this);
        bpText.setText("Blood Sugar: " + bloodsu + " mg/dl");
        bpText.setTextSize(18);
        bpText.setTextColor(Color.BLACK);
        bpText.setGravity(Gravity.CENTER);

        TextView statusText = new TextView(this);
        statusText.setText("Status: " + getStatus(bloodsu));
        statusText.setTextSize(16);
        statusText.setTextColor(Color.GRAY);

        layout.addView(bpText);
        layout.addView(statusText);
        cardView.addView(layout);
        historyContainer.addView(cardView);
    }

    private String getStatus(int bloodsu) {
        if (bloodsu < 70 ) return "Low Blood Sugar";
        if (bloodsu > 140 ) return "High Blood Sugar";
        return "Normal";
    }
}
