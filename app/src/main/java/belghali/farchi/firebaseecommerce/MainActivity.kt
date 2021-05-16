package belghali.farchi.firebaseecommerce

import Beans.Product
import Beans.Product_Facture
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var dataBaseReference : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseDatabase = FirebaseDatabase.getInstance()
        dataBaseReference = firebaseDatabase.getReference("products")
        add_btn.setOnClickListener {
            val newProduct = Product(libelle.text.toString(),description.text.toString(),price.text.toString().toFloat(),qte.text.toString().toInt())
            val ref = UUID.randomUUID().toString()
            dataBaseReference.child(ref).setValue(newProduct).addOnSuccessListener {
                Toast.makeText(applicationContext, "Product Added Successfully", Toast.LENGTH_LONG).show()
                libelle.text.clear()
                description.text.clear()
                price.text.clear()
                qte.text.clear()
            }.addOnFailureListener {
                Toast.makeText(applicationContext, "Error !!!", Toast.LENGTH_LONG).show()
            }
        }
        goToAddFacture.setOnClickListener {
            var intent = Intent(applicationContext, Facture_Create::class.java)
            startActivity(intent)
        }
    }
}