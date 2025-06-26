package location.adapters.driven.sunny2025

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.MongoClient
import location.abstractions.PourLireLesLocations
import location.domain.entities.Location
import org.bson.codecs.pojo.annotations.BsonId
import sunny2025.domain.regles.Remise

class MongoPourLireLesLocations(connexionUrl: String): PourLireLesLocations {

    data class DTOMongoLocations(
        @BsonId
        val id: String,
        val client: String,
        val remise: Remise
     )


    private val mongoClient = MongoClient.Factory.create(connexionUrl)
    private val db = mongoClient.getDatabase("sunny2025")
    private val collection = db.getCollection<DTOMongoLocations>("locations")

    override fun enregistrer(location: Location) {
        val dto = DTOMongoLocations(location.id, location.client, location.remise)
        collection.insertOne(dto)
    }

    override fun nombreDeLocations(client: String): Int {
       return collection.countDocuments().toInt()
    }

    override fun donneMoiLes4DernieresLocations(client: String): List<Location> {
        val filter = Filters.eq("client", client)
        return collection
            .find(filter)
            .map { dto ->
                Location(
                    id = dto.id,
                    client = dto.client,
                    remise = dto.remise
                )
            }
            .toList()
    }
}