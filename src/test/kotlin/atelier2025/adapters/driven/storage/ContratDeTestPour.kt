package atelier2025.adapters.driven.storage

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import location.ports.PourQuelqueChose

abstract class ContratDeTestPour : AnnotationSpec  {

    protected lateinit var sut: PourQuelqueChose

    constructor(sut: PourQuelqueChose) {  // remplacer Any par PourXXX
       this.sut =sut
     }

    constructor(sut: Any) {  // à effacer
    }

    //copier coller les tests écrits dans l'atelier ici
    @Test
    fun   dites_moi_quoi_tester() {
       sut.faireQuelqueChose()    shouldBe true
    }

}