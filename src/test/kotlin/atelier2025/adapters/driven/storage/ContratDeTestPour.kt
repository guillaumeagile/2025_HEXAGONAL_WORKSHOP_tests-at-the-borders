package atelier2025.adapters.driven.storage

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

abstract class ContratDeTestPour : AnnotationSpec  {

   // protected var _sut: Pour

    constructor(sut: Any) {  // remplacer Any par PourXXX
       // _sut =sut
     }

    //copier coller les tests écrits dans l'atelier ici
    @Test
    fun   dites_moi_quoi_tester() {
        0 shouldBe 1
    }

}