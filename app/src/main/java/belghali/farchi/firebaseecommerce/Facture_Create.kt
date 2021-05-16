package belghali.farchi.firebaseecommerce

import Beans.Facture
import Beans.Product
import Beans.Product_Facture
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_facture__create.*
import kotlinx.android.synthetic.main.activity_facture__list.*
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
    private lateinit var selectedFacture: Facture
    private lateinit var selectedKey : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facture__create)
        if (intent.getSerializableExtra("facture") != null && intent.getStringExtra("key") != null) {
            selectedFacture = (intent.getSerializableExtra("facture") as? Facture)!!
            selectedKey = intent.getStringExtra("key").toString()
        }
        chosenProductsWithQuantity = ArrayList()
        if (selectedFacture != null && selectedKey != null) {
            chosenProductsWithQuantity.addAll(selectedFacture.products)
            secondAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, chosenProductsWithQuantity)
            list_Of_Products.adapter = secondAdapter
            client_nameValue.setText(selectedFacture.nom_client)
            client_lastName.setText(selectedFacture.prenom_client)
        }
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
        val builder:AlertDialog.Builder = AlertDialog.Builder(this)
        list_Of_Products.setOnItemClickListener { parent, view, position, id ->
            builder.setTitle("Removing a selected Product")
            builder.setMessage("Are you sure you want to remove the selected Product ?")
            builder.setPositiveButton("Yes", DialogInterface.OnClickListener{ dialog, which ->
                chosenProductsWithQuantity.removeAt(position)
                secondAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, chosenProductsWithQuantity)
                list_Of_Products.adapter = secondAdapter
                dialog.dismiss()
            })
            builder.setNegativeButton("No", DialogInterface.OnClickListener{ dialog, which ->
                dialog.dismiss()
            })
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
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
        if ((client_nameValue.text.toString() != null || client_nameValue.text.toString() == "") && (client_lastName.text.toString() != null || client_lastName.text.toString() == "") && chosenProductsWithQuantity.size != 0) {
            val newFacture = Facture(client_nameValue.text.toString(),client_lastName.text.toString(),chosenProductsWithQuantity)
            dataBaseReference = firebaseDatabase.getReference("factures")
            val ref : String
            if (selectedFacture != null && selectedKey != null) {
                ref = selectedKey
            } else {
                ref = UUID.randomUUID().toString()
            }
            dataBaseReference.child(ref).setValue(newFacture).addOnSuccessListener {
                if (selectedFacture != null && selectedKey != null) {
                    Toast.makeText(applicationContext, "Facture Updated successfully", Toast.LENGTH_LONG).show()
                    val itent = Intent(applicationContext, Facture_List::class.java)
                    startActivity(itent)
                }
                else {
                    Toast.makeText(applicationContext, "Facture Saved successfully", Toast.LENGTH_LONG).show()
                    client_nameValue.text.clear()
                    client_lastName.text.clear()
                    product_quantity.text.clear()
                    chosenProductsWithQuantity = ArrayList()
                    secondAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, chosenProductsWithQuantity)
                    list_Of_Products.adapter = secondAdapter
                }
            }
        } else {
            Toast.makeText(applicationContext, "Please Give your first and last name with chosen Products and Quantities", Toast.LENGTH_LONG).show()
        }
    }
}