package com.example.caredio

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/*class activityMyAcount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
*/
class activityMyAcount : AppCompatActivity() {

    private lateinit var textViewName: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var textViewEmail2: TextView
    private lateinit var textViewPhone: TextView
    private lateinit var textViewAdress: TextView
    private lateinit var textViewGender: TextView
    private lateinit var textViewBirth: TextView
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_acount)

        textViewName = findViewById(R.id.userName)
        textViewEmail = findViewById(R.id.userEmail)
        textViewEmail2 = findViewById(R.id.Textemail)
        textViewPhone = findViewById(R.id.TextPhone)
        textViewAdress = findViewById(R.id.TextAdress)
        textViewGender = findViewById(R.id.TextGender)
        textViewBirth = findViewById(R.id.DateBearth)

        fetchUserName()

    }

    private fun fetchUserName() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("Patients").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val name = document.getString("name")
                        val email = document.getString("email")
                        val phone = document.getString("phone")
                        val adress = document.getString("adress")
                        val gender = document.getString("gender")
                        val date = document.getString("dob")
                        textViewName.text = name
                        textViewEmail.text = email
                        textViewEmail2.text = email
                            textViewPhone.text =phone
                            textViewAdress.text =adress
                            textViewGender.text =gender

                        if (date != null && date.length == 8) {
                            val formattedDate = "${date.substring(0, 2)}/${date.substring(2, 4)}/${date.substring(4, 8)}"
                            textViewBirth.text = formattedDate
                        } else {
                            textViewBirth.text = "Invalid Date"
                        }

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