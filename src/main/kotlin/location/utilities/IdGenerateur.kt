package location.utilities

import location.ports.PourLesIdentifiants
import ulid.ULID

// pour les tests
class TestableIdGenerateur : PourLesIdentifiants {
    private var compteur: Int = 0

    override fun idSuivant(): String {
        compteur++;
       return "fakeId-${compteur}"
    }
}

//  pour la production
class UlidGenerateur : PourLesIdentifiants   {
    override fun idSuivant(): String = ULID.randomULID()
}

// la même chose, mais juste pour la beauté des interfaces fonctionnelles
val ulidGenerateur = PourLesIdentifiants {
    ULID.randomULID()
}
