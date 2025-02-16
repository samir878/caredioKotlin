package com.example.caredio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginDOC : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logindoc)

        // Initialize Firebase Authentication and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val emailEditText = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passwordEditText = findViewById<EditText>(R.id.editTextTextPassword3)
        val loginButton = findViewById<Button>(R.id.button)

        val backButton: FloatingActionButton = findViewById(R.id.floatingActionButton2)
        backButton.setOnClickListener {
            finish() // This will close the current activity and go back
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Sign in the user
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Check if the user is a patient by querying Firestore
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            db.collection("Patients").document(userId).get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        // User is a patient, deny login
                                        Toast.makeText(this, "Login Failed: You are a patient, not a doctor", Toast.LENGTH_SHORT).show()
                                        auth.signOut() // Log the user out immediately
                                    } else {
                                        // User is not a patient (presumably a doctor)
                                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, HomeDoc::class.java))
                                        finish()
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Error checking user role", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
