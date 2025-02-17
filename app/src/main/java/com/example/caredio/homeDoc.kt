package com.example.caredio

import AppointmentAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class HomeDoc : AppCompatActivity() {

    private lateinit var appointmentsRecyclerView: RecyclerView
    private lateinit var appointmentAdapter: AppointmentAdapter
    private val appointmentList = mutableListOf<Appointment>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_doc)

        // Gérer les marges liées aux barres système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val home: ImageView = findViewById(R.id.imageView9)
        val patientli: ImageView = findViewById(R.id.imageView13)
        val profil: ImageView = findViewById(R.id.imageView14)

        // Set click listener for Doctor image
        home.setOnClickListener {
            val intent = Intent(this, HomeDoc::class.java)
            startActivity(intent)
        }
        profil.setOnClickListener {
            val intent = Intent(this, MyprofileDoc::class.java)
            startActivity(intent)
        }

        patientli.setOnClickListener {
            val intent = Intent(this, ListPatient::class.java)
            startActivity(intent)
        }


        // Initialiser le calendrier 📅
        val daysOfMonth = listOf(
            "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
            "1", "2", "3", "4", "5", "6", "7"
        )
        val calendarRecyclerView = findViewById<RecyclerView>(R.id.calendarRecyclerView)

        val calendarAdapter = CalendarAdapter(daysOfMonth) { day ->
            Toast.makeText(this, "Selected: $day", Toast.LENGTH_SHORT).show()
        }

        calendarRecyclerView.layoutManager = GridLayoutManager(this, 7) // 7 colonnes pour une semaine
        calendarRecyclerView.adapter = calendarAdapter

        // Initialiser le RecyclerView pour les rendez-vous 🏥
        appointmentsRecyclerView = findViewById(R.id.appointmentsRecyclerView)
        appointmentsRecyclerView.layoutManager = LinearLayoutManager(this)
        appointmentAdapter = AppointmentAdapter(appointmentList)
        appointmentsRecyclerView.adapter = appointmentAdapter

        // Récupérer les rendez-vous depuis Firestore 🔥
        fetchAppointments()
    }

    private fun fetchAppointments() {
        db.collection("RendezVous")
            .get()
            .addOnSuccessListener { documents ->
                appointmentList.clear()
                for (document in documents) {
                    val firstName = document.getString("firstName") ?: ""
                    val lastName = document.getString("lastName") ?: ""
                    val date = document.getString("date") ?: ""
                    val time = document.getString("time") ?: ""
                    appointmentList.add(Appointment("$firstName $lastName", 0, R.drawable.patient, "$date - $time"))
                }
                appointmentAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erreur lors du chargement des rendez-vous", Toast.LENGTH_SHORT).show()
            }
    }
}
