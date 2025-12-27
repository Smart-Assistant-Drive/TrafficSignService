package com.example.rest.interfaceAdaptersLayer.persistence

import com.example.rest.businessLayer.adapter.sign.SignDataSourceModel
import com.example.rest.businessLayer.boundaries.SignsDataSourceGateway
import com.mongodb.client.model.IndexOptions
import org.bson.Document
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.index.Index

class SignsDataSourceGatewayImpl(
    private val mongoTemplate: MongoTemplate,
) : SignsDataSourceGateway {
    init {
        if (!mongoTemplate.collectionExists("signs")) {
            mongoTemplate.createCollection("signs")
        }
        mongoTemplate.indexOps("signs").ensureIndex(
            Index()
                .on("roadId", Sort.Direction.ASC)
                .on("direction", Sort.Direction.ASC),
        )

        mongoTemplate.execute("signs") { collection ->
            collection.createIndex(
                Document("position", "2d"),
                IndexOptions()
                    .min(-100000.0)
                    .max(100000.0),
            )
        }
    }

    override fun save(requestModel: SignDataSourceModel) {
        val sign =
            Document()
                .append("type", requestModel.type)
                .append("category", requestModel.category)
                .append("roadId", requestModel.idRoad)
                .append("direction", requestModel.direction)
                // IMPORTANT: [x, y] → [longitude, latitude]
                .append("position", listOf(requestModel.longitude, requestModel.latitude))
                .append("lanes", requestModel.lanes)

        if (requestModel.speedLimit != null && requestModel.unit != null) {
            sign
                .append("speedLimit", requestModel.speedLimit)
                .append("unit", requestModel.unit)
        }

        mongoTemplate.getCollection("signs").insertOne(sign)
    }

    override fun findSigns(
        idRoad: String,
        direction: Int,
        latitude: Double,
        longitude: Double,
    ): List<SignDataSourceModel> {
        val query =
            Document()
                .append("roadId", idRoad)
                .append("direction", direction)
                .append(
                    "position",
                    Document("\$near", listOf(longitude, latitude)),
                )

        return getSigns(query)
    }

    override fun findSigns(
        idRoad: String,
        direction: Int,
    ): List<SignDataSourceModel> {
        val query =
            Document()
                .append("roadId", idRoad)
                .append("direction", direction)
        return getSigns(query)
    }

    private fun getSigns(query: Document): List<SignDataSourceModel> {
        val signs = mongoTemplate.getCollection("signs").find(query)

        return signs
            .map { doc ->
                val pos = doc["position"] as List<*>

                SignDataSourceModel(
                    type = doc.getString("type"),
                    category = doc.getString("category"),
                    idRoad = doc.getString("roadId"),
                    direction = doc.getInteger("direction"),
                    longitude = pos[0] as Double,
                    latitude = pos[1] as Double,
                    lanes = doc.getString("lanes"),
                    speedLimit = doc.getInteger("speedLimit"),
                    unit = doc.getString("unit"),
                )
            }.toList()
    }
}
