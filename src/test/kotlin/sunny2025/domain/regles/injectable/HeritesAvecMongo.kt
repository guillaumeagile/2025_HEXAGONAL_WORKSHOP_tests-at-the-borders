package sunny2025.domain.regles.injectable

import location.adapters.driven.sunny2025.MongoPourLireLesLocations
import org.testcontainers.containers.MongoDBContainer

val mongoStockageFactory = {
    // Arrange
    val mongoDB = MongoDBContainer("mongo:8")
    mongoDB.start()
    val connectionUri = mongoDB.connectionString
    MongoPourLireLesLocations(connectionUri)
    // return repo
}

class HeritesAvecMongo : BaseRegleDePrixAvecFidelitéTests(mongoStockageFactory()) {
}
// probleme: une implémentation base de données peut être buggé (ou désalignée avec le fake)
// ou juste un souci d'isolation du test avec les containers
