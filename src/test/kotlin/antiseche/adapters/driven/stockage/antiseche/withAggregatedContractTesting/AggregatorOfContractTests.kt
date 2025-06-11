package antiseche.adapters.driven.stockage.antiseche.withAggregatedContractTesting

import io.kotest.core.spec.style.stringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import location.domain.entities.Ticket
import location.ports.antiseche.PourTickets


// FIND A WAY to bind the Contracts of Tests with the Contract of Implementations

object AggregatorOfContractTests  {

    // Immutable list of all test functions, to be reused by other tests
    val allTests = listOf(
        AggregatorOfContractTests::storageSaveAndCount,
        AggregatorOfContractTests::storageNoSaveAndCount,
        AggregatorOfContractTests::storageSaveAndRead,
        AggregatorOfContractTests::storageSaveTwoAndRead,
        AggregatorOfContractTests::storageCannotSaveTwoOfTheSameId
    )

    fun storageNoSaveAndCount(stockage: PourTickets) = stringSpec {
        "count should return zero tickets" {
            stockage.reset()
            stockage.count().getOrNull() shouldBe 0
        }
    }

    fun storageSaveAndRead(stockage: PourTickets) = stringSpec {
       "getTickets should return the list of saved tickets" {
            stockage.reset()
            val testTicket = Ticket.BuildOne( )
            stockage.save(testTicket)
            stockage.getAll().getOrNull()?.first() shouldBe testTicket
        }
    }

    fun storageSaveTwoAndRead(stockage: PourTickets) = stringSpec {
        "getTickets should return the list of saved tickets ordered by id" {
            stockage.reset()
            val testTicket1 = Ticket.BuildOne( )
            val testTicket2 = Ticket.BuildOne( ).copy(id = "2", usagerId = "2")
            stockage.save(testTicket2)
            stockage.save(testTicket1)
            val actualResultOfGetAll = stockage.getAll().getOrNull()

            actualResultOfGetAll shouldNotBeNull
                    {
                        actualResultOfGetAll?.size shouldBe 2
                        actualResultOfGetAll?.first() shouldBe testTicket1
                        actualResultOfGetAll?.last() shouldBe testTicket2
                    }
        }
    }

    fun storageCannotSaveTwoOfTheSameId(stockage: PourTickets) = stringSpec {
        "saving 2 tickets with the same id should return only the last updated" {
            stockage.reset()
            val testTicket1 = Ticket.BuildOne( )
            val testTicket2 = Ticket.BuildOne( ).copy( usagerId = "2")
            stockage.save(testTicket1)
            stockage.save(testTicket2)
            val actualResultOfGetAll = stockage.getAll().getOrNull()

            actualResultOfGetAll shouldNotBeNull
                    {
                        actualResultOfGetAll?.size shouldBe 1
                        actualResultOfGetAll?.first() shouldBe testTicket2
                    }
        }
    }

    fun storageSaveAndCount(stockage: PourTickets) = stringSpec {
        "count should return the number of saved tickets" {
            stockage.reset()
            stockage.save(Ticket.BuildOne( ))
            stockage.count().getOrNull() shouldBe 1
        }
    }

}



