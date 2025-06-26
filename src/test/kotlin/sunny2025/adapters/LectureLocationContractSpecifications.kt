package sunny2025.adapters

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import location.abstractions.PourLireLesLocations
import location.domain.entities.Location

abstract class LectureLocationContractSpecifications(val sut: PourLireLesLocations)
    : AnnotationSpec(){
    @BeforeEach
    fun beforeTest() {
        println("Before each test")
        sut.reset()  // ca serait mieux
    }

    @Test
    fun    count_should_return_zero_items() {
       sut.nombreDeLocations() shouldBe 0
    }

    @Test
    fun    count_should_return_one_item() {
       sut.enregistrer(Location.BuildOne("Alien"))
       sut.nombreDeLocations("Alien") shouldBe 1
        sut.nombreDeLocations("Alice") shouldBe 0 // nous démontre que
        // l'implémentation Mongo n'est pas correcte !!!!!
    }

}