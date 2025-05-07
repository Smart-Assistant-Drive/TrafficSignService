package com.example.rest.domainLayer.trafficSign

import com.example.rest.domainLayer.LaneReference
import com.example.rest.domainLayer.LaneReferenceFactory
import com.example.rest.domainLayer.TrafficSignCategory
import com.example.rest.domainLayer.TrafficSignType

object NoEntrySign : TrafficSignType {
    override val category: TrafficSignCategory = TrafficSignCategory.REGULATORY_SIGN
    override val laneReference: LaneReference = LaneReferenceFactory.createAllLaneReference()
}
