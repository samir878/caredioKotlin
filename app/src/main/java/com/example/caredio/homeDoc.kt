package com.example.caredio
import AppointmentAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeDoc : AppCompatActivity() {

    private lateinit var appointmentsRecyclerView: RecyclerView
    private lateinit var appointmentAdapter: AppointmentAdapter
    private val appointmentList = mutableListOf<Appointment>()

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

        // Ajouter des rendez-vous avec l'heure 🕒
        appointmentList.add(Appointment("Taylor Swift", 35, R.drawable.taylor_swift, "8:30 AM"))
        appointmentList.add(Appointment("Bruno Mars", 38, R.drawable.bruno_mars, "10:00 AM"))
        appointmentList.add(Appointment("Selena Gomez", 31, R.drawable.selena_gomez, "9:30 AM"))

        // Configurer l'Adapter pour afficher les rendez-vous 📅
        appointmentAdapter = AppointmentAdapter(appointmentList)
        appointmentsRecyclerView.adapter = appointmentAdapter
    }
}
