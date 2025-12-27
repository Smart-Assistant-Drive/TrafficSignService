package com.example.rest.interfaceAdaptersLayer.controllers.dto.createSign

import com.example.rest.businessLayer.adapter.sign.SignModel
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.hateoas.Link
import org.springframework.hateoas.RepresentationModel

data class SignResponseDto
    @JsonCreator
    constructor(
        @param:JsonProperty("type")
        val type: String,
        @param:JsonProperty("category")
        val category: String,
        @param:JsonProperty("idRoad")
        val idRoad: String,
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
    ) : RepresentationModel<SignResponseDto>()

fun SignModel.toDto(links: List<Link>): SignResponseDto =
    SignResponseDto(
        type = type,
        category = category,
        idRoad = idRoad,
        direction = direction,
        latitude = latitude,
        longitude = longitude,
        lanes = lanes,
        speedLimit = speedLimit,
        unit = unit,
    ).add(links)
