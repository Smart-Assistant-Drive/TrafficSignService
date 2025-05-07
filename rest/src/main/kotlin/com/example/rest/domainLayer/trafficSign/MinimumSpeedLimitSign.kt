package com.example.rest.domainLayer.trafficSign

import com.example.rest.domainLayer.LaneReference
import com.example.rest.domainLayer.TrafficSignCategory
import com.example.rest.domainLayer.TrafficSignType

data class MinimumSpeedLimitSign(
    val minimumSpeedLimit: Int,
    val speedUnit: SpeedUnit,
    override val laneReference: LaneReference,
) : TrafficSignType {
    override val category: TrafficSignCategory = TrafficSignCategory.SPEED_LIMIT_SIGN
}
