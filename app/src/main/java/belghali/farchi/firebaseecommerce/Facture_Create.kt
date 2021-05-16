package belghali.farchi.firebaseecommerce

import Beans.Facture
import Beans.Product
import Beans.Product_Facture
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_facture__create.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Facture_Create : AppCompatActivity() {
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var dataBaseReference : DatabaseReference
    private lateinit var products: ArrayList<Product>
    private lateinit var chosenProductsWithQuantity : ArrayList<Product_Facture>
    private lateinit var adapter: ArrayAdapter<Product>
    private lateinit var secondAdapter: ArrayAdapter<Product_Facture>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facture__create)
        chosenProductsWithQuantity = ArrayList()
        addChosenProduct()
        firebaseDatabase = FirebaseDatabase.getInstance()
        dataBaseReference = firebaseDatabase.getReference("products")
        products = ArrayList()
        dataBaseReference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val td : Map<String, Object> = item.getValue() as HashMap<String, Object>
                    val p = Product(
                        td["libelle"].toString(),
                        td["description"].toString(),
                        td["price"].toString().toFloat(), td["qte"].toString().toInt())
                    products.add(p)
                }
                adapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, products)
                product_comboBox.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        save_facture.setOnClickListener {
            saveFactureToFireBase()
        }
    }


    fun addChosenProduct() {
        add_product_ToList_Button.setOnClickListener {
            if (product_comboBox.selectedItem != null && product_quantity.text != null) {
                val selectedProduct = products[product_comboBox.selectedItemPosition]
                val selectedQuantity = product_quantity.text.toString().toInt()
                var product_facture = Product_Facture(selectedProduct, selectedQuantity)
                chosenProductsWithQuantity.add(product_facture)
                secondAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, chosenProductsWithQuantity)
                list_Of_Products.adapter = secondAdapter
            } else {
                Toast.makeText(applicationContext, "Please Select a product and the quantity", Toast.LENGTH_LONG).show()
            }
            }
        }


    fun saveFactureToFireBase() {
        if ((client_name.text.toString() != null || client_name.text.toString() == "") && (client_lastName.text.toString() != null || client_lastName.text.toString() == "") && chosenProductsWithQuantity.size != 0) {
            val newFacture = Facture(client_name.text.toString(),client_lastName.text.toString(),chosenProductsWithQuantity)
            dataBaseReference = firebaseDatabase.getReference("factures")
            val ref = UUID.randomUUID().toString()
            dataBaseReference.child(ref).setValue(newFacture).addOnSuccessListener {
                Toast.makeText(applicationContext, "Facture Saved successfully", Toast.LENGTH_LONG).show()
                client_name.text.clear()
                client_lastName.text.clear()
                product_quantity.text.clear()
                chosenProductsWithQuantity.clear()
            }
        } else {
            Toast.makeText(applicationContext, "Please Give your first and last name with chosen Products and Quantities", Toast.LENGTH_LONG).show()
        }
    }
}