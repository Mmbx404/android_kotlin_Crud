package belghali.farchi.firebaseecommerce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_fire_base_auth.*

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fire_base_auth)
        login_button.setOnClickListener {
            if (email_input.text == null || password_input.text == null) {
                Toast.makeText(applicationContext, "Please Enter and email and a password", Toast.LENGTH_LONG).show()
            } else {
                val email = email_input.text.toString().trim()
                val password = password_input.text.toString()
                val auth = FirebaseAuth.getInstance()
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            val user = auth.currentUser
                            intent.putExtra("user", user)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(applicationContext, task.exception?.message.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}