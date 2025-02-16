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

public class Poids extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private EditText weight;
    private TextView statusText;
    private TextView statusText1;
    private TextView statusText2;

    private Button saveButton;

    private static final String TAG = "BloodPressureActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poids);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Get UI Elements
        weight = findViewById(R.id.editTextText2); // Systolic input

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
        String weightStr = weight.getText().toString().trim();


        // Validate input
        if (weightStr.isEmpty() ) {
            Toast.makeText(this, "Please enter both values!", Toast.LENGTH_SHORT).show();
            return;
        }

        int weight = Integer.parseInt(weightStr);


        // Determine  weight status
        String status = getStatus(weight);
        statusText.setText("Status: " + status);


        String status1 = getStatus1(weight);
        statusText1.setText("weight: " + status1);

        String status2 = getStatus2(weight);
        statusText2.setText( status2);



        // Prepare data for Firebase
        Map<String, Object> weightDocRefData = new HashMap<>();
        weightDocRefData.put("weight", weight);


        // Reference to the 'Blood' subcollection
        DocumentReference weightDocRef = db.collection("Patients")
                .document(userId)
                .collection("Weight")
                .document(); // Auto-generate a document ID

        // Save data in Firebase
        weightDocRef.set(weightDocRefData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "weight saved successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save data!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error saving data", e);
                });
    }

    // Function to determine blood pressure status
    private String getStatus(int weight) {
        if (weight < 50 ) return "Low";
        if (weight > 110 ) return "High";
        return "Normal";
    }
    private String getStatus1(int weight) {
        if (weight < 50) return "Lower Then 90";
        if (weight > 110 ) return "Higher Then 110";
        return "Normal";
    }
    private String getStatus2(int weight) {
        if (weight < 50) return "Your weight is above the normal";
        if (weight > 110 ) return "Your weight is lower the normal";
        return "Your weight is normal";
    }

}
