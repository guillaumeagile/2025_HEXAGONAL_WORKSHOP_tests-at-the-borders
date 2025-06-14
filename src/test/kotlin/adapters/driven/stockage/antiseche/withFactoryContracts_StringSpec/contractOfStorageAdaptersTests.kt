package adapters.driven.stockage.antiseche.withFactoryContracts_StringSpec


import io.kotest.core.spec.style.stringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import location.domain.entities.Ticket
import location.ports.antiseche.PourTickets

fun contractOfStorageAdaptersTests( stockage: PourTickets) = stringSpec {

    beforeTest {
        println("Reset data before Executing $it")
        stockage.reset()
    }

    "count should return zero tickets" {
        stockage.count().getOrNull() shouldBe 0
    }

    "getTickets should return the list of saved tickets" {
        val testTicket = Ticket.BuildOne( )
        stockage.save(testTicket)
        stockage.getAll().getOrNull()?.first() shouldBe testTicket
    }

    "getTickets should return the list of saved tickets ordered by id" {
        val testTicket1 = Ticket.BuildOne( )
        val testTicket2 = Ticket.BuildOne( ) .copy(id = "2", usagerId = "2")
        stockage.save(testTicket2) // 2 saved in first position
        stockage.save(testTicket1)
        val actualResultOfGetAll = stockage.getAll().getOrNull()

        actualResultOfGetAll shouldNotBeNull
                {
                    actualResultOfGetAll?.size shouldBe 2
                    actualResultOfGetAll?.first() shouldBe testTicket1
                    actualResultOfGetAll?.last() shouldBe testTicket2
                }
    }

    "saving 2 tickets with the same id should return only the last updated" {
        val testTicket1 = Ticket.BuildOne( )
        val testTicket2 =Ticket.BuildOne( ).copy( usagerId = "2")
        stockage.save(testTicket1)
        stockage.save(testTicket2)
        val actualResultOfGetAll = stockage.getAll().getOrNull()

        actualResultOfGetAll shouldNotBeNull
                {
                    actualResultOfGetAll?.size shouldBe 1
                    actualResultOfGetAll?.first() shouldBe testTicket2
                }
    }

    "count should return the number of saved tickets" {
        stockage.save(Ticket.BuildOne( ))
        stockage.count().getOrNull() shouldBe 1
    }
}