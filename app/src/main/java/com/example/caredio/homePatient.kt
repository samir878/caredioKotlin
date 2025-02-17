package com.example.caredio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomePatient : AppCompatActivity() {

    private lateinit var textViewName: TextView
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_patient)
        val DiagoButton: Button = findViewById(R.id.button5)
        val DiagoButtonw: Button = findViewById(R.id.button6)
        val DiagoButtonSU: Button = findViewById(R.id.button7)
        val DiagoButtonH: Button = findViewById(R.id.button4)

        // Set click listener for Doctor image
        DiagoButton.setOnClickListener {
            val intent = Intent(this, DiagoBloodSu::class.java)
            startActivity(intent)
        }

        DiagoButtonw.setOnClickListener {
            val intent = Intent(this, PoidsDiagno::class.java)
            startActivity(intent)
        }
        DiagoButtonSU.setOnClickListener {
            val intent = Intent(this, BloodSuDiagno::class.java)
            startActivity(intent)
        }
        DiagoButtonH.setOnClickListener {
            val intent = Intent(this, HeartDiago::class.java)
            startActivity(intent)
        }

        val myprofile: ImageView = findViewById(R.id.imageView8)

        // Set click listener for Doctor image
        myprofile.setOnClickListener {
            val intent = Intent(this, activityMyAcount::class.java)
            startActivity(intent)
        }

        textViewName = findViewById(R.id.textView11)
        fetchUserName()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchUserName() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("Patients").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val name = document.getString("name")
                        textViewName.text = "Welcome ${name ?: "Guest"}"
                    } else {
                        textViewName.text = "Welcome Guest"
                    }
                }
                .addOnFailureListener { e ->
                    textViewName.text = "Error: ${e.message}"
                }
        } else {
            textViewName.text = "User not logged in"
        }
    }

}