package location.abstractions

interface IRequestHandler<TCommand, TResponse> {

   suspend fun handle(demande: TCommand) : TResponse

}
