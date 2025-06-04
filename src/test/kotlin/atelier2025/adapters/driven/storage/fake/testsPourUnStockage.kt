package atelier2025.adapters.driven.storage.fake

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import location.ports.Pour

class testsPourUnStockage : AnnotationSpec {

    private var _sut: Pour

    constructor(){
        _sut = UnStockage()
    }

    @BeforeEach
    fun beforeTest() {
        println("Before each test")
       // _sut.reset() // ca serait bien de le tester lui aussi
    }

    @Test
    fun   dites_moi_quoi_tester() {
       0 shouldBe 1
    }








    //constructor(sut: Pour ) {
    //    _sut =sut
    // }
}