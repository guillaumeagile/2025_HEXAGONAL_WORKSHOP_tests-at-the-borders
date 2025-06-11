package adapters.driven.stockage.antiseche.withInheritedAnnotationSpecs

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import location.domain.entities.Ticket
import location.ports.antiseche.PourTickets

// specification de contrat de tous les tests avec Annotation
 abstract class StorageContractSpecification : AnnotationSpec {

    private var _sut: PourTickets

    constructor(sut: PourTickets ) {
        _sut = sut
    }

    @BeforeEach
    fun beforeTest() {
        println("Before each test")
        _sut.reset()
    }

    @Test
    fun    count_should_return_zero_tickets() {
        _sut.count().getOrNull() shouldBe 0
    }

    @Test
    fun    count_should_return_the_number_of_saved_tickets() {
        _sut.save(Ticket.BuildOne( ))
        _sut.count().getOrNull() shouldBe 1
    }

    @Test
    fun    getTickets_should_return_the_list_of_saved_tickets() {
        val testTicket = Ticket.BuildOne( )
        _sut.save(testTicket)
        _sut.getAll().getOrNull()?.first() shouldBe testTicket
    }

    @Test
    fun    getTickets_should_return_the_list_of_saved_tickets_ordered_by_id() {
        val testTicket1 = Ticket.BuildOne( )
        val testTicket2 = Ticket.BuildOne( ).copy(id = "2", usagerId = "2")
        _sut.save(testTicket2)
        _sut.save(testTicket1)
        _sut.getAll().getOrNull()?.first() shouldBe testTicket1
        _sut.getAll().getOrNull()?.last() shouldBe testTicket2
    }

    @Test
    fun    saving_2_tickets_with_the_same_id_should_return_only_the_last_updated() {
        val testTicket1 = Ticket.BuildOne( )
        val testTicket2 = Ticket.BuildOne( ).copy( usagerId = "2")
        _sut.save(testTicket1)
        _sut.save(testTicket2)
        _sut.getAll().getOrNull()?.first() shouldBe testTicket2
    }


}