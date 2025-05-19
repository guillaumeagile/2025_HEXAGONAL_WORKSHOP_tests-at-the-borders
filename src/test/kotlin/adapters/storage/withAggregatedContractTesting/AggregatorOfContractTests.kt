package adapters.storage.withAggregatedContractTesting

import io.kotest.core.spec.style.stringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import location.adapters.driven.storage.DTOs.TicketDto
import location.ports.ITicketRepository


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

    fun storageNoSaveAndCount(stockage: ITicketRepository) = stringSpec {
        "count should return zero tickets" {
            stockage.count().getOrNull() shouldBe 0
        }
    }

    fun storageSaveAndRead(stockage: ITicketRepository) = stringSpec {
       "getTickets should return the list of saved tickets" {
            val testTicket = TicketDto(2, 3)
            stockage.save(testTicket)
            stockage.getAll().getOrNull()?.first() shouldBe testTicket
        }
    }

    fun storageSaveTwoAndRead(stockage: ITicketRepository) = stringSpec {
        "getTickets should return the list of saved tickets ordered by id" {
            val testTicket1 = TicketDto(1, 3)
            val testTicket2 = TicketDto(2, 4)
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

    fun storageCannotSaveTwoOfTheSameId(stockage: ITicketRepository) = stringSpec {
        "saving 2 tickets with the same id should return only the last updated" {
            val testTicket1 = TicketDto(1, 3)
            val testTicket2 = TicketDto(1, 4)
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

    fun storageSaveAndCount(stockage: ITicketRepository) = stringSpec {
        "count should return the number of saved tickets" {

            stockage.save(TicketDto(1, 2))
            stockage.count().getOrNull() shouldBe 1
        }
    }

}



