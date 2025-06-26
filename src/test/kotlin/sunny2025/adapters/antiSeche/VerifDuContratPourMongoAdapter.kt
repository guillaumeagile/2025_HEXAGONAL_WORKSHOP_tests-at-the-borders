package sunny2025.adapters.antiSeche

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


class VerifDuContratPourMongoAdapter : LectureLocationContractSpecifications( mongoStockageFactory())  {
}