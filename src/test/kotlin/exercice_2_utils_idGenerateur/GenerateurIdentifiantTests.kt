package exercice_2_utils_idGenerateur

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import location.utilities.TestableIdGenerateur

class GenerateurIdentifiantTests : StringSpec({

    "le generateur doit delivrer une valeur" .config(enabled = true) {

        val sut = TestableIdGenerateur()
        val idObtenu =  sut.idSuivant()

        idObtenu shouldBe "fakeId-1"
    }

    "le generateur ne peut delivrer deux fois la meme valeur" .config(enabled = true) {

        val sut = TestableIdGenerateur()
        val idObtenu1 = sut.idSuivant()
        val idObtenu2 = sut.idSuivant()

        idObtenu1 shouldNotBe idObtenu2
    }
})