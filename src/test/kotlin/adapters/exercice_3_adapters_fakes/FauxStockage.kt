package adapters.exercice_3_adapters_fakes

import location.ports.ITicketRepository

import location.adapters.driven.storage.DTOs.TicketDto

class FauxStockage : ITicketRepository {

    val listDesTickets = mutableListOf<TicketDto>()

    override fun saveTicket(ticket: TicketDto) = runCatching {
        listDesTickets.add(ticket)
    }

    override fun countTickets(): Result<Int> = Result.success(listDesTickets.size)

    override fun getTickets(): Result<List<TicketDto>> = Result.success( listDesTickets)

}


