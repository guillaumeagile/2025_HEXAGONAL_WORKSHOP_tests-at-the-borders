package catalog.domain.valueObjects

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import catalog.domain.errors.ErreurDeCatalog

@JvmInline
value class Prix private constructor(val enCentimes: Int) {
    companion object {
        fun de(centimes: Int): Either<ErreurDeCatalog, Prix> =
            if (centimes >= 0) Prix(centimes).right()
            else ErreurDeCatalog.PrixInvalide.left()

        fun reconstituer(centimes: Int): Prix = Prix(centimes)
    }

    fun enEuros(): Double = enCentimes / 100.0
}
