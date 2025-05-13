package com.example.rest.businessLayer.adapter.sign

data class SignModel(
    val type: String,
    val category: String,
    val idRoad: Int,
    val direction: Int,
    val latitude: Double,
    val longitude: Double,
    val lanes: String,
    val speedLimit: Int?,
    val unit: String?,
)
