package location.adapters.driven

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import location.ports.PourAvoirHeure


class VraieHorloge : PourAvoirHeure {
    override fun quelleHeureEstIl(): LocalDateTime {
       return Clock.System.now().toLocalDateTime( TimeZone.UTC)
    }

}
