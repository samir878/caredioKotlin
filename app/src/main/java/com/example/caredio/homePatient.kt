package com.example.caredio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomePatient : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_patient)
        val DiagoButton: Button = findViewById(R.id.button5)
        val DiagoButtonw: Button = findViewById(R.id.button6)

        // Set click listener for Doctor image
        DiagoButton.setOnClickListener {
            val intent = Intent(this, DiagoBloodSu::class.java)
            startActivity(intent)
        }

        DiagoButtonw.setOnClickListener {
            val intent = Intent(this, PoidsDiagno::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}