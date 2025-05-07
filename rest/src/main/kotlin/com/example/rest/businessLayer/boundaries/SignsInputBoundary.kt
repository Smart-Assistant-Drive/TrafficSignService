package com.example.rest.businessLayer.boundaries

interface SignsInputBoundary {
    fun createSign(requestModel: Any): Result<Any>

    fun getSigns(
        idRoad: Int,
        direction: Int,
        latitude: Double,
        longitude: Double,
    ): Result<Any>
}
