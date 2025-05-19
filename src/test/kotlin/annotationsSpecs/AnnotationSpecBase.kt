package annotationsSpecs

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

// voir si on peut faire héritage avec annotationSpec
// ou bien ré-utiliser les fonctions
open class AnnotationSpecBase : AnnotationSpec {

    private var _sut: Int

    constructor() {
        _sut = 1
    }

    constructor(sut: Int ) {
        _sut = sut
    }

    @BeforeEach
    fun beforeTest() {
        println("Before each test")
    }

    @Test
    fun test1() {
        _sut shouldBe 1
    }

    @Test
    fun test2() {
        3 shouldBe 3
    }
}