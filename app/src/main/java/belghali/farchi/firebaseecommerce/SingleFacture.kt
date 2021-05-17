package belghali.farchi.firebaseecommerce

import Beans.Facture
import Beans.Product
import Beans.Product_Facture
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_facture__create.*
import kotlinx.android.synthetic.main.activity_single_facture.*
import kotlinx.android.synthetic.main.nav_single_facture.*

class SingleFacture : AppCompatActivity() {
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var dataBaseReference : DatabaseReference
    private lateinit var adapter: ArrayAdapter<Product_Facture>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_single_facture)
        initDrawerFunctions()
        val selectedFacture = intent.getSerializableExtra("facture") as? Facture
        val selectedKey = intent.getStringExtra("key")
        if (selectedKey != null && selectedFacture != null) {
            last_name_value.setText(selectedFacture.prenom_client)
            first_name_value.setText(selectedFacture.nom_client)
            adapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, selectedFacture.products)
            products_facture_list.adapter = adapter
            delete_facture_button.setOnClickListener {
                deleteFactureFromFireBase(selectedKey)
            }
            edit_facture_button.setOnClickListener {
                sendFactureToEdit(selectedFacture, selectedKey)
            }
        }
    }

    fun deleteFactureFromFireBase(key : String) {
        firebaseDatabase = FirebaseDatabase.getInstance()
        dataBaseReference = firebaseDatabase.getReference("factures")
        dataBaseReference.child(key).equalTo(key).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.ref.removeValue().addOnSuccessListener {
                    Toast.makeText(applicationContext, "Facture Removed Successfully", Toast.LENGTH_LONG).show()
                    val intent = Intent(applicationContext, Facture_List::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun sendFactureToEdit(facture: Facture, key : String) {
        val intent = Intent(this, Facture_Create::class.java)
        intent.putExtra("facture", facture)
        intent.putExtra("key", key)
        startActivity(intent)
        finish()
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