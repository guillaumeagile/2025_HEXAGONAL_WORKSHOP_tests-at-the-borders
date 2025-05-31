package zEssais.annotationsSpecs_ExampleSimple

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Disabled

// specification de contrat de tous les tests avec Annotation
 abstract class ContractSpecification : AnnotationSpec {

    private var _sut: Int


    constructor(sut: Int ) {
        _sut = sut
    }

    @BeforeEach
    fun beforeTest() {
        println("Before each test")
    }

    @Test
    @Disabled
    fun test1() {
        _sut shouldBe 1
    }

    @Test
    @Disabled
    fun test2() {
        _sut shouldNotBe 9
    }
}