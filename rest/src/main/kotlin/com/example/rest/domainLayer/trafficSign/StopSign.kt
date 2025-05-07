package com.example.rest.domainLayer.trafficSign

import com.example.rest.domainLayer.LaneReference
import com.example.rest.domainLayer.TrafficSignCategory
import com.example.rest.domainLayer.TrafficSignType

data class StopSign(
    override val laneReference: LaneReference,
) : TrafficSignType {
    override val category: TrafficSignCategory = TrafficSignCategory.REGULATORY_SIGN
}
