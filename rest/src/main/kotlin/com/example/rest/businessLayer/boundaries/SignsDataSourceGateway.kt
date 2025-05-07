package com.example.rest.businessLayer.boundaries

import com.example.rest.businessLayer.adapter.sign.SignDataSourceModel

interface SignsDataSourceGateway {
    fun save(requestModel: SignDataSourceModel)

    fun findSigns(
        idRoad: Int,
        direction: Int,
        latitude: Double,
        longitude: Double,
    ): List<SignDataSourceModel>
}
