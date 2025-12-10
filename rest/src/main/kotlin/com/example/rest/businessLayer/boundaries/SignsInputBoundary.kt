package com.example.rest.businessLayer.boundaries

import com.example.rest.businessLayer.adapter.sign.SignModel

interface SignsInputBoundary {
    fun allSignsTypes(): Result<List<String>>

    fun createSign(requestModel: SignModel): Result<SignModel>

    fun getSignsNear(
        idRoad: Int,
        direction: Int,
        latitude: Double,
        longitude: Double,
    ): Result<List<SignModel>>

    fun getSigns(
        idRoad: Int,
        direction: Int,
    ): Result<List<SignModel>>
}
