package adapters.autres_adapters_fakes

import kotlinx.datetime.LocalDateTime
import location.ports.ILesHorloges


class FausseHorloge(val leMaintenant: LocalDateTime) : ILesHorloges {

    override fun quelleHeureEstIl(): LocalDateTime = leMaintenant

}

