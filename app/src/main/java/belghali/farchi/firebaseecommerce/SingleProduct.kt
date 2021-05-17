package belghali.farchi.firebaseecommerce

import Beans.Product
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_single_product.*
import kotlinx.android.synthetic.main.nav_single_product.*

class SingleProduct : AppCompatActivity() {
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var dataBaseReference : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_single_product)
        initDrawerFunctions()
        val selectedProduct = intent.getSerializableExtra("product") as? Product
        val selectedKey = intent.getStringExtra("productKey")
        if (selectedKey != null && selectedProduct != null) {
            libelle_Value.text = selectedProduct.libelle
            description_value.text = selectedProduct.description
            price_value.text = selectedProduct.price.toString()
            quantity_value.text = selectedProduct.qte.toString()
            Toast.makeText(applicationContext, "This is the Key $selectedKey", Toast.LENGTH_LONG).show()
            delete_Product.setOnClickListener {
                deleteProductFromFireBase(selectedKey)
            }
            edit_product.setOnClickListener {
                sendProductToEdit(selectedProduct, selectedKey)
            }
        }
    }

    private fun deleteProductFromFireBase(key : String) {
        firebaseDatabase = FirebaseDatabase.getInstance()
        dataBaseReference = firebaseDatabase.getReference("products")
        dataBaseReference.child(key).equalTo(key).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.ref.removeValue().addOnSuccessListener {
                    Toast.makeText(applicationContext, "Product Removed Successfully", Toast.LENGTH_LONG).show()
                    val intent = Intent(applicationContext, Products_List::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun sendProductToEdit(product: Product, key : String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("product", product)
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