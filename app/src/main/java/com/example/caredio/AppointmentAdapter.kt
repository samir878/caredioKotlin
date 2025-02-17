
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.caredio.Appointment
import com.example.caredio.R

class AppointmentAdapter(private val appointments: List<Appointment>) :
    RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    class AppointmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeTextView: TextView = view.findViewById(R.id.timeTextView)
        val profileImageView: ImageView = view.findViewById(R.id.profileImageView)
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        //val ageTextView: TextView = view.findViewById(R.id.ageTextView)
        val medicalRecordsTextView: TextView = view.findViewById(R.id.medicalRecordsTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.timeTextView.text = appointment.time
        holder.nameTextView.text = appointment.name
        //holder.ageTextView.text = "${appointment.age} yo"
        holder.profileImageView.setImageResource(appointment.imageResId)

        // Ajout du texte "medical records >"
        holder.medicalRecordsTextView.text = "medical records >"

        // Si tu veux ajouter un clic pour afficher le dossier médical
        holder.medicalRecordsTextView.setOnClickListener {
            // Ajoute ici l'action à faire (ex: ouvrir un nouvel écran)
        }
    }

    override fun getItemCount() = appointments.size
}
