package adapters.storage.withContractTesting

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

// voir si on peut faire héritage avec annotationSpec
// ou bien ré-utiliser les fonctions
class AnnotationSpecExample : AnnotationSpec() {

    @BeforeEach
    fun beforeTest() {
        println("Before each test")
    }

    @Test
    fun test1() {
        1 shouldBe 1
    }

    @Test
    fun test2() {
        3 shouldBe 3
    }
}