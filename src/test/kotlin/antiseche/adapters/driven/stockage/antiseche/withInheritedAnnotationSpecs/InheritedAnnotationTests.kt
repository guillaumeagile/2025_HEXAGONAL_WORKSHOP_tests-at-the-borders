package antiseche.adapters.driven.stockage.antiseche.withInheritedAnnotationSpecs

import com.redis.testcontainers.RedisContainer
import location.adapters.driven.antiseche.mongoDb.RepositoryMongoDb
import location.adapters.driven.antiseche.postGreSQL.TicketSqlRepository
import location.adapters.driven.antiseche.valKey.ValKeyAdapter
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

//class InheritedAnnotationTests_WithFake : StorageContractSpecification(FakeAdapterPourTicket()) {}   // le fake n'est pas compatible

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