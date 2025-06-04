package adapters.driven.stockage.antiseche.withInheritedAnnotationSpecs

import location.adapters.driven.antiseche.mongoDb.RepositoryMongoDb
import location.adapters.driven.antiseche.postGreSQL.TicketSqlRepository
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.PostgreSQLContainer

val sqlStockageFactory = {
    // Arrange
    val postgres = PostgreSQLContainer("postgres:16")
    postgres.start()
    val repo = TicketSqlRepository(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
    repo.createTableTicket()
    // return repo
    repo
}

val mongoStockageFactory = {
    // Arrange
    val mongoDB = MongoDBContainer("mongo:8")
    mongoDB.start()
    val connectionUri = mongoDB.connectionString
    RepositoryMongoDb(connectionUri)
    // return repo
}


class InheritedAnnotationTests_WithMongoDB : StorageContractSpecification(mongoStockageFactory()) {}

class InheritedAnnotationTests_WithSQL : StorageContractSpecification(sqlStockageFactory()) {}