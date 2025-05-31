package location.ports

import location.adapters.driven.storage.DTOs.TicketDto

// dans une branche de développement, on va utiliser une interface
// interface  PourStocker<TEntity> where TEntity : Entity
// Entity doit avoir un ID

interface PourStocker { //CQS

    // write
    fun save(ticket: TicketDto): Result<Boolean>
    // read
    fun count(): Result<Int>
    fun getAll(): Result<List<TicketDto>>
    // unfortunately, I created this only for test purposes
    fun reset(): Result<Boolean>


}

//  séparation des responsabilités entre query(read) et write => CQRS = CQS + ISP

// 2e marche: faire un 2e repo (+ adapter) pour une autre entité, qui justifie encore plus CQRS