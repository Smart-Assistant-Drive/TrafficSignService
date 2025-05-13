package com.example.rest.domainLayer.trafficSign

import com.example.rest.domainLayer.LaneReference
import com.example.rest.domainLayer.TrafficSignCategory
import com.example.rest.domainLayer.TrafficSignType
import com.example.rest.domainLayer.lane.AllLaneReference

data class MaximumSpeedLimitSign(
    val maximumSpeedLimit: Int,
    val speedUnit: SpeedUnit,
) : TrafficSignType {
    override val category: TrafficSignCategory = TrafficSignCategory.SPEED_LIMIT_SIGN
    override val laneReference: LaneReference = AllLaneReference
}
