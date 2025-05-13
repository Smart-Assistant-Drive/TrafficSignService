package com.example.rest.domainLayer.trafficSign

import com.example.rest.domainLayer.LaneReference
import com.example.rest.domainLayer.TrafficSignCategory
import com.example.rest.domainLayer.TrafficSignType
import com.example.rest.domainLayer.lane.AllLaneReference

object NoEntrySign : TrafficSignType {
    override val category: TrafficSignCategory = TrafficSignCategory.REGULATORY_SIGN
    override val laneReference: LaneReference = AllLaneReference
}
