package com.example.caredio

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginPAT : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_pat)

        // Change Status Bar Color Programmatically
        val window: Window = window
        window.statusBarColor = resources.getColor(R.color.main)

        // Initialize Firebase Authentication and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val emailEditText = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passwordEditText = findViewById<EditText>(R.id.editTextTextPassword3)
        val Signup = findViewById<TextView>(R.id.textView5)
        val loginButton = findViewById<Button>(R.id.button)

        val backButton: FloatingActionButton = findViewById(R.id.floatingActionButton2)
        backButton.setOnClickListener {
            finish() // This will close the current activity and go back
        }

        Signup.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
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
                        // Check if the user is a patient
                        val userId = auth.currentUser!!.uid
                        checkUserRole(userId)
                    } else {
                        Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    // Function to check if the user is a patient
    private fun checkUserRole(userId: String) {
        db.collection("Patients").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val role = document.getString("role")
                    if (role == "patient") {
                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomePatient::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "User is not a patient!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "User is not a patient!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error getting user role: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
