package Beans

class Facture(
    var nom_client : String,
    var prenom_client: String,
    var products : ArrayList<Product_Facture>,
) {
    override fun toString(): String {
        return "Facture(nom_client='$nom_client', prenom_client='$prenom_client', products=$products)"
    }
}