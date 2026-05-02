package catalog.domain.valueObjects

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import catalog.domain.errors.ErreurDeCatalog

@JvmInline
value class NomDeProduit private constructor(val valeur: String) {
    companion object {
        fun de(valeur: String): Either<ErreurDeCatalog, NomDeProduit> =
            if (valeur.isNotBlank()) NomDeProduit(valeur.trim()).right()
            else ErreurDeCatalog.NomVide.left()

        fun reconstituer(valeur: String): NomDeProduit = NomDeProduit(valeur)
    }
}
