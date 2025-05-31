package location.ports.antiseche

import location.adapters.driven.storage.DTOs.TicketDto

interface PourStocker { //CQS

    // write
    fun save(ticket: TicketDto): Result<Boolean>
    // read
    fun count(): Result<Int>
    fun getAll(): Result<List<TicketDto>>
    // unfortunately, I created this only for test purposes
    fun reset(): Result<Boolean>


}