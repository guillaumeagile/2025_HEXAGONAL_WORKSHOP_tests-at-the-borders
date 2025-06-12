package atelier2025.adapters.driven.storage.verifierLeContratPourTous

import atelier2025.adapters.driven.storage.ContratDeTestPour
import atelier2025.adapters.driven.storage.fake.UnEspionEtFakeDePersistence
import atelier2025.adapters.driven.storage.pasFake.RepositoryMongoDbPourQuelqueChose
import location.adapters.driven.antiseche.postGreSQL.TicketSqlRepository
import location.ports.PourPersisterUnTicket
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.PostgreSQLContainer

// on setup les adaptateurs qui doivent respect le contrat de tests

val fakeStorageFactory = { UnEspionEtFakeDePersistence() }

val mongoStockageFactory : () -> PourPersisterUnTicket  = {
    // Arrange
    // 1 - container
    val mongoDB = MongoDBContainer("mongo:8")
    mongoDB.start()
    val connectionUri = mongoDB.connectionString

    // return repo directement (instanciation en Kotlin, pas de new)
    RepositoryMongoDbPourQuelqueChose(connectionUri) // l'adapteur a besoin de connaitre la connexionString
    //CHANGER POUR NOUVELLE CLASSE D ADAPTEUR MONGO qui soit aligné avec le port 'Pour' (but de l'atelier)
}



// et maintenant héritons du contrat, ca va faire jouer tous les (même) tests pour les différents adapteurs

class InheritedAnnotationTests_WithFake : ContratDeTestPour(fakeStorageFactory()) {}

class InheritedAnnotationTests_WithMongoDB : ContratDeTestPour(mongoStockageFactory()  ) {}

//on peut essayer de tester un autre adaptateur, par exemple qui utiliserait ValKey (ex Redis)


//class InheritedAnnotationTests_WithSQL : ContratDeTestPour(sqlStockageFactory()) {}
// on pourrait aussi utiliser un adaptateur SQL pour PostgreSQL
val sqlStockageFactory = {
    // Arrange
    //  1 - containers
    val postgres = PostgreSQLContainer("postgres:16") // c'est pas la bonne interface, faut le ré écrire
    postgres.start()

    // 2- adapter  -> CHANGER POUR NOUVEL ADAPTER
    val repo = TicketSqlRepository(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
    // besoin de créer les schemas de données parceque c'est du SQL
    repo.createTableTicket()
    // return repo
    repo
}