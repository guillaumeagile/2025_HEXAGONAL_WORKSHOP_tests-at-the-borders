package location.domain.entities

import io.nacular.measured.units.Measure
import io.nacular.measured.units.Time
import location.domain.valueObjects.Monnaie


// en francais , parce que c'est métier, renommer en BorneLocation
class ParcMetre(val Id: String) {


    //que fait il / quelles sont ses responsabilités
    fun EditerTicker(duree: Measure<Time>): Ticket {
        TODO()
    }

    fun EditerTicker(argent: Monnaie): Ticket {
        TODO("Not yet implemented")
    }


}
