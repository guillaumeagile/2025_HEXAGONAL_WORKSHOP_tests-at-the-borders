package catalog.domain.valueObjects

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import catalog.domain.errors.ErreurDeCatalog

@JvmInline
value class Slug private constructor(val valeur: String) {
    companion object {
        fun de(valeur: String): Either<ErreurDeCatalog, Slug> =
            if (valeur.isNotBlank()) Slug(valeur.trim().lowercase()).right()
            else ErreurDeCatalog.SlugVide.left()

        fun reconstituer(valeur: String): Slug = Slug(valeur)
    }
}
