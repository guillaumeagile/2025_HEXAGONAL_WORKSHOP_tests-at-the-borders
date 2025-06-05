package atelier2025.adapters.driven.storage.fake

import atelier2025.adapters.driven.storage.EspionPourStockage
import atelier2025.adapters.driven.storage.InMemoryStockage
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import location.domain.entities.Ticket
import location.ports.PourStockage

class TestsPourInMemory : AnnotationSpec {

    private var _sut:  InMemoryStockage  // PourStockage

    constructor() {
        _sut = InMemoryStockage() //EspionPourStockage() //
    }

    @Test
    fun si_rien_alors_rien() {

        _sut.length shouldBe 0
    }

    @Test
    fun si_je_sauve_alors_il_y_a_1() {

        _sut.StockerTicket(Result.success(Ticket.enEchec()))
        _sut.length shouldBe 1
    }


    @BeforeEach
    fun beforeTest() {
        println("Before each test")
         _sut.reset() // ca serait bien de le tester lui aussi
    }


}