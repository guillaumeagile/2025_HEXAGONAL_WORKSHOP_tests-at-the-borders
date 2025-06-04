package atelier2025.adapters.driven.storage.fake

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import location.ports.Pour

class TestsPourUnStockage : AnnotationSpec {

    private var _sut: Pour

    constructor(){
        _sut = UnStockage()
    }

    @Test
    fun   dites_moi_quoi_tester() {
       0 shouldBe 1
    }





    @BeforeEach
    fun beforeTest() {
        println("Before each test")
        // _sut.reset() // ca serait bien de le tester lui aussi
    }



}