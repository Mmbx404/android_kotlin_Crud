package Beans

class Product(
    var libelle : String = "Default",
    var description : String = "Default",
    var price : Float = 0.0F,
    var qte : Int = 0,
) {
    override fun toString(): String {
        return libelle
    }

}