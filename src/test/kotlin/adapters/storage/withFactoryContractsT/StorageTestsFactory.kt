package adapters.storage.withFactoryContractsT

import io.kotest.core.spec.style.StringSpec
import location.adapters.driven.storage.mongoDb.RepositoryMongoDb
import location.adapters.driven.storage.postGreSQL.TicketSqlRepository
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.PostgreSQLContainer


class StorageTestsFactory  : StringSpec (   {

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


    include( "test avec Mongo", contractOfStorageAdaptersTests( mongoStockageFactory()))

    include( "test avec PostGreSQL",  contractOfStorageAdaptersTests(sqlStockageFactory()))


})