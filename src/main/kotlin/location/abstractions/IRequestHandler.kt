package location.abstractions

interface IRequestHandler<Tin, Tout> {

   suspend fun handle(demande: Tin) : Tout

}
