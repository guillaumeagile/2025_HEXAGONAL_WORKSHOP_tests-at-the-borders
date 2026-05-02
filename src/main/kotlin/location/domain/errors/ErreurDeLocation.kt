package location.domain.errors

sealed interface ErreurDeLocation {
    data object DureeInvalide : ErreurDeLocation
    data object IdManquant : ErreurDeLocation
    data object PrixInvalide : ErreurDeLocation
    data class TicketIntrouvable(val id: String) : ErreurDeLocation
}
