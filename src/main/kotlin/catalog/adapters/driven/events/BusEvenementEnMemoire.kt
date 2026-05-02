package catalog.adapters.driven.events

import catalog.domain.events.EvenementDeCatalog
import catalog.ports.driven.PourPublierUnEvenement

// In-process event bus. Handlers are registered at bootstrap time.
// Swap this for a Kafka/RabbitMQ adapter without changing any domain or application code.
class BusEvenementEnMemoire(
    private val handlers: List<suspend (EvenementDeCatalog) -> Unit> = emptyList()
) : PourPublierUnEvenement {

    override suspend fun publier(evenement: EvenementDeCatalog) {
        handlers.forEach { it(evenement) }
    }
}
