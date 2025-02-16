package com.example.caredio;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BloodPressureActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private EditText systolicInput, diastolicInput;
    private TextView statusText;
    private TextView statusText1;
    private TextView statusText2;
    private TextView statusText3;
    private Button saveButton;

    private static final String TAG = "BloodPressureActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blood);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Get UI Elements
        systolicInput = findViewById(R.id.editTextText2); // Systolic input
        diastolicInput = findViewById(R.id.editTextText3); // Diastolic input
        statusText = findViewById(R.id.textView29); // Blood pressure status
        statusText1 = findViewById(R.id.textView30); // Blood pressure status
        statusText2 = findViewById(R.id.textView31); // Blood pressure status
        statusText3 = findViewById(R.id.textView32); // Blood pressure status
        saveButton = findViewById(R.id.button10); // Save button

        // Save data when button is clicked
        saveButton.setOnClickListener(v -> saveBloodPressureData());
    }

    private void saveBloodPressureData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid(); // Get the logged-in user's ID

        // Get values from input fields
        String systolicStr = systolicInput.getText().toString().trim();
        String diastolicStr = diastolicInput.getText().toString().trim();

        // Validate input
        if (systolicStr.isEmpty() || diastolicStr.isEmpty()) {
            Toast.makeText(this, "Please enter both values!", Toast.LENGTH_SHORT).show();
            return;
        }

        int systolic = Integer.parseInt(systolicStr);
        int diastolic = Integer.parseInt(diastolicStr);

        // Determine blood pressure status
        String status = getStatus(systolic, diastolic);
        statusText.setText("Status: " + status);


        String status1 = getStatus1(systolic, diastolic);
        statusText1.setText("Systolic: " + status1);

        String status2 = getStatus2(systolic, diastolic);
        statusText2.setText("Diastolic: " + status2);
        String status3 = getStatus3(systolic, diastolic);
        statusText3.setText( status3);

        // Prepare data for Firebase
        Map<String, Object> bloodPressureData = new HashMap<>();
        bloodPressureData.put("systolic", systolic);
        bloodPressureData.put("diastolic", diastolic);

        // Reference to the 'Blood' subcollection
        DocumentReference bloodDocRef = db.collection("Patients")
                .document(userId)
                .collection("Blood")
                .document(); // Auto-generate a document ID

        // Save data in Firebase
        bloodDocRef.set(bloodPressureData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Blood pressure saved successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save data!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error saving data", e);
                });
    }

    // Function to determine blood pressure status
    private String getStatus(int systolic, int diastolic) {
        if (systolic < 90 || diastolic < 60) return "Low";
        if (systolic > 140 || diastolic > 90) return "High";
        return "Normal";
    }
    private String getStatus1(int systolic, int diastolic) {
        if (systolic < 90 || diastolic < 60) return "Lower Then 90";
        if (systolic > 140 || diastolic > 90) return "Higher Then 140";
        return "Normal";
    }
    private String getStatus2(int systolic, int diastolic) {
        if (systolic < 90 || diastolic < 60) return "Lower Then 60";
        if (systolic > 140 || diastolic > 90) return "Higher Then 90";
        return "Normal";
    }

    private String getStatus3(int systolic, int diastolic) {
        if (systolic < 90 || diastolic < 60) return "Your Blood Pressure is below the normal";
        if (systolic > 140 || diastolic > 90) return "Your Blood Pressure is Higher the normal";
        return "Your Blood Pressure is normal";
    }
}
