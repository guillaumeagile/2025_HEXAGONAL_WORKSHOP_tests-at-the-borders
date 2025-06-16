package antiseche.adapters.driven.stockage.antiseche.withInheritedAnnotationSpecs

import antiseche.adapters.driven.stockage.antiseche.FakeAdapterPourTicket
import com.redis.testcontainers.RedisContainer
import location.adapters.driven.antiseche.mongoDb.RepositoryMongoDb
import location.adapters.driven.antiseche.postGreSQL.TicketSqlRepository
import location.adapters.driven.antiseche.valKey.ValKeyAdapter
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.PostgreSQLContainer

// Create a singleton PostgreSQL container
class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:16")

// Singleton instance
object PostgresqlTestContainer {
    val instance by lazy {
        PostgresContainer().apply {
            start()
        }
    }
}

val sqlStockageFactory = {
    // Use the shared PostgreSQL container instance
    val postgres = PostgresqlTestContainer.instance
    val repo = TicketSqlRepository(postgres.jdbcUrl, postgres.username, postgres.password)
    // Make sure the table exists
    repo.createTableTicket()
    println("PostgreSQL container started at ${postgres.jdbcUrl}")
    // Print connection details for debugging
    println("PostgreSQL connection: ${postgres.jdbcUrl}, user: ${postgres.username}")
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

class InheritedAnnotationTests_WithFake : StorageContractSpecification(FakeAdapterPourTicket()) {}   // le fake n'est pas compatible

class InheritedAnnotationTests_WithMongoDB : StorageContractSpecification(mongoStockageFactory()) {}

class InheritedAnnotationTests_WithSQL : StorageContractSpecification(sqlStockageFactory()) {}

class InheritedAnnotationTests_WitValkey : StorageContractSpecification(valKeyFactory()) {}

val valKeyFactory = {
    // Arrange
    // Using Redis container for Valkey (they're compatible)
    val redisContainer =  RedisContainer(
            RedisContainer.DEFAULT_IMAGE_NAME.withTag(RedisContainer.DEFAULT_TAG));
    redisContainer.start()

    val redisUrl = redisContainer.getRedisURI();

    // Create and return the ValKeyAdapter
    ValKeyAdapter(redisUrl)
}