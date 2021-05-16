package Beans

import java.io.Serializable

class Facture(
    var nom_client : String = "",
    var prenom_client: String = "",
    var products : ArrayList<Product_Facture> = ArrayList(),
) : Serializable {
    override fun toString(): String {
        return "Client : $nom_client $prenom_client"
    }
    constructor() : this("","", ArrayList<Product_Facture>())
}