package adapters.autres_adapters_fakes

import kotlinx.datetime.LocalDateTime
import location.ports.PourAvoirHeure


class FausseHorloge(val leMaintenant: LocalDateTime) : PourAvoirHeure {

    override fun quelleHeureEstIl(): LocalDateTime = leMaintenant

}

