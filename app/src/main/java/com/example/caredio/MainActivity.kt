package com.example.caredio

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Thread.sleep(3000)
        //    installSplashScreen()
        setContentView(R.layout.activity_main)
        // Find the ImageView elements
        // Change Status Bar Color Programmatically
        val window: Window = window
        window.statusBarColor = resources.getColor(R.color.main)
        val doctorImageView: ImageView = findViewById(R.id.imageView2)
        val patientImageView: ImageView = findViewById(R.id.imageView4)

        // Set click listener for Doctor image
        doctorImageView.setOnClickListener {
            val intent = Intent(this, LoginDOC::class.java)
            startActivity(intent)
        }

        // Set click listener for Patient image
        patientImageView.setOnClickListener {
            val intent = Intent(this, LoginPAT::class.java)
            startActivity(intent)
        }
    }
}
