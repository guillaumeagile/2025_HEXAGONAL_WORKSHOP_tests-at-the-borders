package location.ports

import kotlinx.datetime.LocalDateTime


interface PourAvoirHeure {

    fun quelleHeureEstIl(): LocalDateTime

}
