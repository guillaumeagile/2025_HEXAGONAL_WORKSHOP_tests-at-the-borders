package adapters.storage.fake

import location.ports.ITicketRepository

import location.adapters.driven.storage.DTOs.TicketDto

class FauxStockage : ITicketRepository {

    val listDesTickets = mutableListOf<TicketDto>()

    override fun save(ticket: TicketDto) = runCatching {
        listDesTickets.add(ticket)
    }

    override fun count(): Result<Int> = Result.success(listDesTickets.size)

    override fun getAll(): Result<List<TicketDto>> = Result.success( listDesTickets)

}


