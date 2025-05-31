package location.utilities

import location.ports.IJeDonneDesIdentifiants
import ulid.ULID


class TestableIdGenerateur : IJeDonneDesIdentifiants {

    private var compteur: Int = 0

    override fun idSuivant(): String {
        compteur++;
       return "fakeId-${compteur}"
    }

}


class ulidGenerateur : IJeDonneDesIdentifiants   {
    override fun idSuivant(): String = ULID.randomULID()
}

// juste pour la beauté des interfaces fonctionnelles
val UlidGenerateur = IJeDonneDesIdentifiants {
    ULID.randomULID()
}
