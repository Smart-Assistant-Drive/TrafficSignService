package com.example.rest.businessLayer.boundaries

import com.example.rest.businessLayer.adapter.sign.SignModel

interface SignsInputBoundary {
    fun allSignsTypes(): Result<List<String>>

    fun createSign(requestModel: SignModel): Result<SignModel>

    fun getSignsNear(
        idRoad: String,
        direction: Int,
        latitude: Double,
        longitude: Double,
    ): Result<List<SignModel>>

    fun getSigns(
        idRoad: String,
        direction: Int,
    ): Result<List<SignModel>>
}
