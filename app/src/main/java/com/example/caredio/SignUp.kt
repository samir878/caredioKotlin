package com.example.caredio


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {
    private var nameET: EditText? = null
    private var emailET: EditText? = null
    private var dobET: EditText? = null
    private var phoneET: EditText? = null
    private var adressET: EditText? = null
    private var genderET: EditText? = null
    private var passwordET: EditText? = null
    private var confirmPasswordET: EditText? = null
    private lateinit var signUpBtn: Button
    private var mAuth: FirebaseAuth? = null
    private var db: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize Firebase Auth & Firestore
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Link XML elements
        nameET = findViewById<EditText>(R.id.editTextText)
        emailET = findViewById<EditText>(R.id.editTextTextEmailAddress2)
        dobET = findViewById<EditText>(R.id.editTextDate)
        phoneET = findViewById<EditText>(R.id.editTextPhone)
        adressET = findViewById<EditText>(R.id.editTextAdress)
        genderET = findViewById<EditText>(R.id.editTextGender)
        passwordET = findViewById<EditText>(R.id.editTextTextPassword)
        confirmPasswordET = findViewById<EditText>(R.id.editTextTextPassword2)
        signUpBtn = findViewById(R.id.button3)

        //retour
        val backButton: FloatingActionButton = findViewById(R.id.floatingActionButton2)
        backButton.setOnClickListener {
            finish() // This will close the current activity and go back
        }

        // Set Click Listener
        signUpBtn.setOnClickListener {
            registerUser() // Called when the button is clicked
        }

    }

    private fun registerUser() {
        val name = nameET!!.text.toString().trim { it <= ' ' }
        val email = emailET!!.text.toString().trim { it <= ' ' }
        val dob = dobET!!.text.toString().trim { it <= ' ' }
        val phone = phoneET!!.text.toString().trim { it <= ' ' }
        val adress = adressET!!.text.toString().trim { it <= ' ' }
        val gender = genderET!!.text.toString().trim { it <= ' ' }
        val role="patient"
        val password = passwordET!!.text.toString().trim { it <= ' ' }
        val confirmPassword = confirmPasswordET!!.text.toString().trim { it <= ' '

        }

        // Validate inputs
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(dob) ||
            TextUtils.isEmpty(phone) ||TextUtils.isEmpty(adress)||TextUtils.isEmpty(gender)|| TextUtils.isEmpty(password) || TextUtils.isEmpty(
                confirmPassword
            )
        ) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // Register in Firebase Authentication
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Get user ID
                    val userId = mAuth!!.currentUser!!.uid

                    // Create patient object
                    val patient: MutableMap<String, Any> =
                        HashMap()
                    patient["userId"] = userId
                    patient["name"] = name
                    patient["email"] = email
                    patient["dob"] = dob
                    patient["phone"] = phone
                    patient["adress"] = adress
                    patient["gender"] = gender
                    patient["role"] = role

                    // Store in Firestore
                    db!!.collection("Patients").document(userId).set(patient)
                        .addOnCompleteListener { task1: Task<Void?> ->
                            if (task1.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Sign-Up Successful!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(
                                    Intent(
                                        this,
                                        HomePatient::class.java
                                    )
                                )
                                finish()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Error saving data: " + task1.exception!!.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        this,
                        "Registration failed: " + task.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}