package belghali.farchi.firebaseecommerce

import Beans.Product
import Beans.Product_Facture
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_main_activity.*
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var dataBaseReference : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_main_activity)
        initDrawerFunctions()
        firebaseDatabase = FirebaseDatabase.getInstance()
        dataBaseReference = firebaseDatabase.getReference("products")
        val selectedProduct = intent.getSerializableExtra("product") as? Product
        val selectedKey = intent.getStringExtra("key")
        if (selectedProduct != null && selectedKey != null) {
            libelle.setText(selectedProduct.libelle)
            description.setText(selectedProduct.description)
            price.setText(selectedProduct.price.toString())
            qte.setText(selectedProduct.qte.toString())
        }
        add_btn.setOnClickListener {
            val newProduct = Product(libelle.text.toString(),description.text.toString(),price.text.toString().toFloat(),qte.text.toString().toInt())
            val ref : String
            if (selectedProduct != null && selectedKey != null) {
                ref = selectedKey
            }
            else {
                ref = UUID.randomUUID().toString()
            }
            dataBaseReference.child(ref).setValue(newProduct).addOnSuccessListener {
                if (selectedProduct != null && selectedKey != null) {
                    Toast.makeText(applicationContext, "Product Updated Successfully", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "Product Added Successfully", Toast.LENGTH_LONG).show()
                }
                libelle.text.clear()
                description.text.clear()
                price.text.clear()
                qte.text.clear()
                val intent = Intent(applicationContext, Products_List::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(applicationContext, "Error !!!", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun initDrawerFunctions() {
        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.create_product_nav_item -> {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.create_facture_nav_item -> {
                    val intent = Intent(applicationContext, Facture_Create::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.facture_list_nav_item -> {
                    val intent = Intent(applicationContext, Facture_List::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.prodcut_list_nav_item -> {
                    val intent = Intent(applicationContext, Products_List::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
         }
    }
}