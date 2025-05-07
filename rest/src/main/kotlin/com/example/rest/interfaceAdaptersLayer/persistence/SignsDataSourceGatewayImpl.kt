package com.example.rest.interfaceAdaptersLayer.persistence

import com.example.rest.businessLayer.adapter.sign.SignDataSourceModel
import com.example.rest.businessLayer.boundaries.SignsDataSourceGateway
import org.bson.Document
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.index.GeospatialIndex
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

        mongoTemplate.indexOps("signs").ensureIndex(
            GeospatialIndex("position"),
        )
    }

    override fun save(requestModel: SignDataSourceModel) {
        val sign =
            Document()
                .append("type", requestModel.type)
                .append("category", requestModel.category)
                .append("roadId", requestModel.idRoad)
                .append("direction", requestModel.direction)
                .append(
                    "position",
                    Document("type", "Point")
                        .append("coordinates", listOf(requestModel.longitude, requestModel.latitude)),
                ).append("lanes", requestModel.lanes)
        if (requestModel.speedLimit != null && requestModel.unit != null) {
            sign
                .append("speedLimit", requestModel.speedLimit)
                .append("unit", requestModel.unit)
        }
        mongoTemplate.getCollection("signs").insertOne(
            sign,
        )
    }

    override fun findSigns(
        idRoad: Int,
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
                    Document(
                        "\$near",
                        Document(
                            "\$geometry",
                            Document("type", "Point")
                                .append("coordinates", listOf(longitude, latitude)),
                        ),
                    ),
                )
        val signs = mongoTemplate.getCollection("signs").find(query)
        return signs
            .map { doc ->
                SignDataSourceModel(
                    type = doc.getString("type"),
                    category = doc.getString("category"),
                    idRoad = doc.getInteger("roadId"),
                    direction = doc.getInteger("direction"),
                    latitude = (doc.get("position") as Document).getList("coordinates", Double::class.java)[1],
                    longitude = (doc.get("position") as Document).getList("coordinates", Double::class.java)[0],
                    lanes = doc.getString("lanes"),
                    speedLimit = doc.getInteger("speedLimit"),
                    unit = doc.getString("unit"),
                )
            }.toList()
    }
}
