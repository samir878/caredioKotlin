package com.example.caredio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ListPatient extends AppCompatActivity {
    private FirebaseFirestore db;
    private LinearLayout historyContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_patient);

        db = FirebaseFirestore.getInstance();
        historyContainer = findViewById(R.id.historyContainer);

        loadPatients();
    }

    private void loadPatients() {
        db.collection("Patients")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String patientId = document.getId();
                            String patientName = document.getString("name"); // Ensure 'name' exists in Firestore

                            addPatientToUI(patientId, patientName);
                        }
                    } else {
                        Log.e("Firestore", "Error getting patients", task.getException());
                    }
                });
    }

    private void addPatientToUI(String patientId, String patientName) {
        TextView textView = new TextView(this);
        textView.setText(patientName != null ? patientName : "Unknown Patient");
        textView.setTextSize(18);
        textView.setPadding(16, 16, 16, 16);
        textView.setOnClickListener(v -> openPatientDetails(patientId));

        historyContainer.addView(textView);
    }

    private void openPatientDetails(String patientId) {
        Intent intent = new Intent(this, ChooseType.class); // Replace with your actual activity
        intent.putExtra("PATIENT_ID", patientId);
        startActivity(intent);
    }
}
