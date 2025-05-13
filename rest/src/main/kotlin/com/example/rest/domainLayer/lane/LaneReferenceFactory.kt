package com.example.rest.domainLayer.lane

import com.example.rest.domainLayer.LaneReference

data class SingleLaneReference(
    val laneNumber: Int,
) : LaneReference {
    override fun isValidForLane(laneNumber: Int): Boolean = laneNumber == this.laneNumber
}

data class RangeLaneReference(
    val startLaneNumber: Int,
    val endLaneNumber: Int,
) : LaneReference {
    override fun isValidForLane(laneNumber: Int): Boolean = laneNumber in startLaneNumber..endLaneNumber
}

data class FromLaneReference(
    val fromLaneNumber: Int,
) : LaneReference {
    override fun isValidForLane(laneNumber: Int): Boolean = laneNumber >= fromLaneNumber
}

data class ToLaneReference(
    val toLaneNumber: Int,
) : LaneReference {
    override fun isValidForLane(laneNumber: Int): Boolean = laneNumber in 1..toLaneNumber
}

object AllLaneReference : LaneReference {
    override fun isValidForLane(laneNumber: Int): Boolean = true
}
