package catalog.ports.driven

import catalog.domain.events.EvenementDeCatalog

fun interface PourPublierUnEvenement {
    suspend fun publier(evenement: EvenementDeCatalog)
}
