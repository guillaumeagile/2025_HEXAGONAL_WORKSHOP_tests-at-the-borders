package location.domain.valueObjects

data class Monnaie(val valeur: Double, val devise: Devises) {
    
    constructor(valeur: Int, devise: Devises) : this(valeur.toDouble(), devise)

    companion object {
        const val DOLLAR_VERS_EUROS = 2
        fun Euros(value: Int): Monnaie = Monnaie(value, Devises.EUROS)
        fun Euros(value: Double): Monnaie = Monnaie(value, Devises.EUROS)
        fun Dollars(value: Int): Monnaie = Monnaie(value, Devises.DOLLARS)
        fun Dollars(value: Double): Monnaie = Monnaie(value, Devises.DOLLARS)
       }

    fun EnEuros(): Double = if (this.devise == Devises.DOLLARS) this.valeur * DOLLAR_VERS_EUROS else this.valeur


    override fun equals(other: Any?): Boolean {
        if (other !is Monnaie)
            return false
        if (this.devise == other.devise)
            return (this.valeur == other.valeur)
        if (this.devise == Devises.DOLLARS && other.devise == Devises.EUROS)
            return (this.valeur * DOLLAR_VERS_EUROS == other.valeur)
        return false
    }

    override fun hashCode(): Int {
        var result = valeur
        result = 31.0 * result + devise.hashCode()
        return result.toInt()
    }
}

enum class Devises {
    DOLLARS,
    EUROS

}
