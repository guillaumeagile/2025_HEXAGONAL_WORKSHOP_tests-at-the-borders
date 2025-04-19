package location.commandHandlers

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import location.abstractions.IRequestHandler
import location.commandHandlers.data.DemandeDuTicket
import location.commandHandlers.data.ReponseALaDemandeDuTicket

class AcheterUnTicketAuParcmetre : IRequestHandler<DemandeDuTicket, ReponseALaDemandeDuTicket> {

    override suspend fun handle(demande: DemandeDuTicket): ReponseALaDemandeDuTicket = coroutineScope {
        println("on demarre le request handler, ca va prendre du temps")

        //inscrire ici l'appel métier

        //puis l'appel à l'adapter de stockage
        launchDotPrinter(150) // c'est un exemple, dans la vraie vie on va appeler le stockage qui est "lent"

         ReponseALaDemandeDuTicket(
            resultat = Result.failure(TODO("faites passer ce test au vert"))
            //resultat = Result.success(Ticket.bidon())
        )
    }





    suspend fun launchDotPrinter( times : Int, char: Char = '.') = coroutineScope {
      println("appel long (${times})")
        repeat(times) {
            delay(10)  // Delay for 10 milliseconds
            print(char)
        }
    }
}



