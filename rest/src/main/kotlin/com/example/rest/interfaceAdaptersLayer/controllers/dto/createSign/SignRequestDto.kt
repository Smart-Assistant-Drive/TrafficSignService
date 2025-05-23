package com.example.rest.interfaceAdaptersLayer.controllers.dto.createSign

import com.example.rest.businessLayer.adapter.sign.SignModel
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class SignRequestDto
    @JsonCreator
    constructor(
        @param:JsonProperty("type")
        val type: String,
        @param:JsonProperty("category")
        val category: String,
        @param:JsonProperty("idRoad")
        val idRoad: Int,
        @param:JsonProperty("direction")
        val direction: Int,
        @param:JsonProperty("latitude")
        val latitude: Double,
        @param:JsonProperty("longitude")
        val longitude: Double,
        @param:JsonProperty("lanes")
        val lanes: String,
        @param:JsonProperty("speedLimit")
        val speedLimit: Int?,
        @param:JsonProperty("unit")
        val unit: String?,
    )

fun SignRequestDto.toModel(): SignModel =
    SignModel(
        type = type,
        category = category,
        idRoad = idRoad,
        direction = direction,
        latitude = latitude,
        longitude = longitude,
        lanes = lanes,
        speedLimit = speedLimit,
        unit = unit,
    )
