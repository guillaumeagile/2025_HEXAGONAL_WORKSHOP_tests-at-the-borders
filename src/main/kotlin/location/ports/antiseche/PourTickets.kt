package location.ports.antiseche

import location.domain.entities.Ticket

interface PourTickets { //CQS


    // write
    fun save(ticket: Ticket): Result<Ticket>

    // read
    fun count(): Result<Int>
    fun getAll(): Result<List<Ticket>>
    // unfortunately, I created this only for test purposes
    fun reset(): Result<Boolean>


}