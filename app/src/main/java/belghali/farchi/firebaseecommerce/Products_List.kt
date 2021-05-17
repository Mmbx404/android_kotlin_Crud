package belghali.farchi.firebaseecommerce

import Beans.Product
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_facture__create.*
import kotlinx.android.synthetic.main.nav_product_list.*

class Products_List : AppCompatActivity() {
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var dataBaseReference : DatabaseReference
    private lateinit var products: ArrayList<Product>
    private lateinit var adapter: ArrayAdapter<Product>
    private lateinit var keys : ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_product_list)
        initDrawerFunctions()
        firebaseDatabase = FirebaseDatabase.getInstance()
        dataBaseReference = firebaseDatabase.getReference("products")
        products = ArrayList()
        keys = ArrayList()
        dataBaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val td : Map<String, Object> = item.getValue() as HashMap<String, Object>
                    val p = Product(
                            td["libelle"].toString(),
                            td["description"].toString(),
                            td["price"].toString().toFloat(), td["qte"].toString().toInt())
                    products.add(p)
                    item.key?.let { keys.add(it) }
                }
                adapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, products)
                list_Of_Products.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        list_Of_Products.setOnItemClickListener { parent, view, position, id ->
            val selectedProduct = products[position]
            val selectedProductKey = keys[position]
            val intent = Intent(this, SingleProduct::class.java)
            intent.putExtra("product", selectedProduct)
            intent.putExtra("productKey", selectedProductKey)
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