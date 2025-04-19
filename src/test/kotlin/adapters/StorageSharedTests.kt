package adapters

import io.kotest.core.spec.style.funSpec
import io.kotest.matchers.shouldBe
import location.ports.ITicketRepository
import location.adapters.driven.storage.DTOs.TicketDto

object StorageSharedTests
{
    fun storageSaveAndCount(stockage: ITicketRepository) = funSpec {
        test("cardinalityTickets should return the number of saved tickets") {

            stockage.save(TicketDto(1, 2))
            stockage.count().getOrNull() shouldBe 1
        }
    }

    fun storageSaveAndRead(stockage: ITicketRepository) = funSpec {
        test("getTickets should return the list of saved tickets") {
            val testTicket = TicketDto(2, 3)
            stockage.save(testTicket)
            stockage.getAll().getOrNull()?.first() shouldBe testTicket
        }
    }
}

