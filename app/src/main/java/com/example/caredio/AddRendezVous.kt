package com.example.caredio

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AddRendezVous : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private var selectedDate: String = ""
    private var selectedTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_rendez_vous)

        window.statusBarColor = resources.getColor(R.color.main)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val firstNameEditText = findViewById<EditText>(R.id.editTextFirstName)
        val lastNameEditText = findViewById<EditText>(R.id.editTextLastName)
        val ageEditText = findViewById<EditText>(R.id.editTextAge)
        dateEditText = findViewById(R.id.editTextSelectDate)
        timeEditText = findViewById(R.id.editTextSelectTime)
        val confirmButton = findViewById<Button>(R.id.buttonConfirm)

        val calendar = Calendar.getInstance()

        dateEditText.setOnClickListener {
            val datePicker = DatePickerDialog(
                this, { _, year, month, dayOfMonth ->
                    selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                    dateEditText.setText(selectedDate)
                    Log.d("DEBUG", "selectedDate après sélection : $selectedDate")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        timeEditText.setOnClickListener {
            val timePicker = TimePickerDialog(
                this, { _, hourOfDay, minute ->
                    selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                    timeEditText.setText(selectedTime)
                    Log.d("AddRendezVous", "Heure sélectionnée : $selectedTime")
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePicker.show()
        }

        confirmButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()
            val age = ageEditText.text.toString().trim()

            selectedDate = dateEditText.text.toString().trim()
            selectedTime = timeEditText.text.toString().trim()

            if (firstName.isEmpty()) {
                firstNameEditText.error = "Veuillez entrer votre prénom"
                return@setOnClickListener
            }
            if (lastName.isEmpty()) {
                lastNameEditText.error = "Veuillez entrer votre nom"
                return@setOnClickListener
            }
            if (age.isEmpty()) {
                ageEditText.error = "Veuillez entrer votre âge"
                return@setOnClickListener
            }
            if (selectedDate.isEmpty()) {
                Toast.makeText(this, "Veuillez sélectionner une date", Toast.LENGTH_SHORT).show()
                Log.e("DEBUG", "selectedDate est vide même après saisie")
                return@setOnClickListener
            }
            if (selectedTime.isEmpty()) {
                Toast.makeText(this, "Veuillez sélectionner une heure", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = auth.currentUser
            if (user == null) {
                Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show()
                Log.e("AddRendezVous", "Utilisateur non connecté")
                return@setOnClickListener
            }
            val userId = user.uid

            val appointment = hashMapOf(
                "userId" to userId,
                "firstName" to firstName,
                "lastName" to lastName,
                "age" to age,
                "date" to selectedDate,
                "time" to selectedTime,
                "timestamp" to SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.FRANCE).format(Date())
            )

            Log.d("AddRendezVous", "Objet rendez-vous à enregistrer : $appointment")

            db.collection("RendezVous").add(appointment)
                .addOnSuccessListener {
                    Toast.makeText(this, "Rendez-vous enregistré avec succès", Toast.LENGTH_SHORT).show()
                    Log.d("AddRendezVous", "Rendez-vous ajouté avec succès")
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erreur : ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("AddRendezVous", "Erreur lors de l'ajout du rendez-vous : ${e.message}")
                }
        }
    }
}
