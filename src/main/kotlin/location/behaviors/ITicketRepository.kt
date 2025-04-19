package location.behaviors

import location.models.DTOs.TicketDto

// dans une branche de développement, on va utiliser une interface
// interface ITicketRepository<TEntity> where TEntity : IEntity
// IEntity doit avoir un ID

interface ITicketRepository { //CQS
    // write
    fun saveTicket(ticket: TicketDto): Result<Boolean>
    // read
    fun countTickets(): Result<Int>
    fun getTickets(): Result<List<TicketDto>>

}

//  séparation des responsabilités entre query(read) et write => CQRS = CQS + ISP

// 2e marche: faire un 2e repo (+ adapter) pour une autre entité, qui justifie encore plus CQRS