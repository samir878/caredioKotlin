package com.example.caredio;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BloodSugarADD extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private EditText bloodsu;
    private TextView statusText;
    private TextView statusText1;
    private TextView statusText2;

    private Button saveButton;

    private static final String TAG = "BloodPressureActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_sugar_add);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Get UI Elements
        bloodsu = findViewById(R.id.editTextText2); // Systolic input

        statusText = findViewById(R.id.textView29); // Blood pressure status
        statusText1 = findViewById(R.id.textView30); // Blood pressure status
        statusText2 = findViewById(R.id.textView32); // Blood pressure status

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
        String bloodsutStr = bloodsu.getText().toString().trim();


        // Validate input
        if (bloodsutStr.isEmpty() ) {
            Toast.makeText(this, "Please enter  value!", Toast.LENGTH_SHORT).show();
            return;
        }

        int bloodsu = Integer.parseInt(bloodsutStr);


        // Determine  weight status
        String status = getStatus(bloodsu);
        statusText.setText("Status: " + status);


        String status1 = getStatus1(bloodsu);
        statusText1.setText("Blood sugar: " + status1);

        String status2 = getStatus2(bloodsu);
        statusText2.setText( status2);



        // Prepare data for Firebase
        Map<String, Object> bloodsuDocRefData = new HashMap<>();
        bloodsuDocRefData.put("blood sugar", bloodsu);


        // Reference to the 'Blood' subcollection
        DocumentReference weightDocRef = db.collection("Patients")
                .document(userId)
                .collection("Bloodsu")
                .document(); // Auto-generate a document ID

        // Save data in Firebase
        weightDocRef.set(bloodsuDocRefData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Blood sugar saved successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save data!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error saving data", e);
                });
    }

    // Function to determine blood pressure status
    private String getStatus(int bloodsu) {
        if (bloodsu < 70 ) return "Low";
        if (bloodsu > 140 ) return "High";
        return "Normal";
    }
    private String getStatus1(int bloodsu) {
        if (bloodsu < 70) return "Lower Then 90";
        if (bloodsu > 140 ) return "Higher Then 110";
        return "Normal";
    }
    private String getStatus2(int bloodsu) {
        if (bloodsu < 70) return "Your Blood sugar is below the normal";
        if (bloodsu > 140 ) return "Your Blood sugar is Higher the normal";
        return "Your Blood sugar is normal";
    }

}
