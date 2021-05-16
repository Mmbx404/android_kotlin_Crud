package Beans

class Product_Facture(
    var product : Product,
    var product_quantity : Int,
) {
    override fun toString(): String {
        return "${product.libelle}, Quantity=$product_quantity"
    }
}