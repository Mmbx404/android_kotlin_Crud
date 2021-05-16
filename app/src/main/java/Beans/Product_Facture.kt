package Beans

import java.io.Serializable

class Product_Facture(
    var product : Product,
    var product_quantity : Int,
) : Serializable{
    override fun toString(): String {
        return "${product.libelle}, Quantity=$product_quantity"
    }

    constructor() : this(Product(),0)
}