package atelier2025.adapters.driven.storage.verifierLeContratPourTous

import atelier2025.adapters.driven.storage.ContratDeTest
import location.adapters.driven.antiseche.mongoDb.RepositoryMongoDb
import location.adapters.driven.antiseche.postGreSQL.TicketSqlRepository
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.PostgreSQLContainer

val sqlStockageFactory = {
    // Arrange
    //  1 - containers
    val postgres = PostgreSQLContainer("postgres:16")
    postgres.start()

    // 2- adapter  -> CHANGER POUR NOUVEL ADAPTER
    val repo = TicketSqlRepository(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
    // besoin de créer les schemas de données parceque c'est du SQL
    repo.createTableTicket()
    // return repo
    repo
}

val mongoStockageFactory = {
    // Arrange
    // 1 - container
    val mongoDB = MongoDBContainer("mongo:8")
    mongoDB.start()
    val connectionUri = mongoDB.connectionString

    // return repo directement (instanciation en Kotlin, pas de new)
    RepositoryMongoDb(connectionUri) // l'adapteur a besoin de connaitre la connexionString
    //CHANGER POUR NOUVELLE CLASSE D ADAPTEUR MONGO qui soit aligné avec le port 'Pour' (but de l'atelier)
}

val fakeStorageFactory = { TODO() } //mais c'est assez simple :)

// et maintenant héritons du contrat, ca va faire jouer tous les (même) tests pour les différents adapteurs

class InheritedAnnotationTests_WithFake : ContratDeTest(fakeStorageFactory()) {}

class InheritedAnnotationTests_WithMongoDB : ContratDeTest(mongoStockageFactory()) {}

class InheritedAnnotationTests_WithSQL : ContratDeTest(sqlStockageFactory()) {}