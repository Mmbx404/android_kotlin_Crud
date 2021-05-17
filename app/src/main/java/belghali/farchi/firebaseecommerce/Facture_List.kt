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
import kotlinx.android.synthetic.main.activity_facture__list.*
import kotlinx.android.synthetic.main.nav_facture_list.*

class Facture_List : AppCompatActivity() {
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var dataBaseReference: DatabaseReference
    private lateinit var factures: ArrayList<Facture>
    private lateinit var adapter: ArrayAdapter<Facture>
    private lateinit var facutureKeys: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_facture_list)
        initDrawerFunctions()
        firebaseDatabase = FirebaseDatabase.getInstance()
        dataBaseReference = firebaseDatabase.getReference("factures")
        factures = ArrayList()
        facutureKeys = ArrayList()
        dataBaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val f = item.getValue(Facture::class.java)
                    if (f != null) {
                        factures.add(f)
                    }
                    item.key?.let { facutureKeys.add(it) }
                }
                adapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, factures)
                list_Of_Factures.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        list_Of_Factures.setOnItemClickListener { parent, view, position, id ->
            val selectedFacture = factures[position]
            val selectedFactureKey = facutureKeys[position]
            Toast.makeText(applicationContext, "Selected Facture : $selectedFacture, selected Key : $selectedFactureKey", Toast.LENGTH_LONG).show()
            val intent = Intent(this, SingleFacture::class.java)
            intent.putExtra("facture", selectedFacture)
            intent.putExtra("key", selectedFactureKey)
            startActivity(intent)
            finish()
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