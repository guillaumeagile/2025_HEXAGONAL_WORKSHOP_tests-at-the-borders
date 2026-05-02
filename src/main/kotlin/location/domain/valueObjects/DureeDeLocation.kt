package location.domain.valueObjects

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import location.domain.errors.ErreurDeLocation

@JvmInline
value class DureeDeLocation private constructor(val enMinutes: Int) {

    companion object {
        fun de(minutes: Int): Either<ErreurDeLocation, DureeDeLocation> =
            if (minutes > 0) DureeDeLocation(minutes).right()
            else ErreurDeLocation.DureeInvalide.left()
    }
}
