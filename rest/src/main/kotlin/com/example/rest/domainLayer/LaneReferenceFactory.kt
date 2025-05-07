package com.example.rest.domainLayer

object LaneReferenceFactory {
    fun createSingleLaneReference(laneNumberValidity: Int): LaneReference =
        object : LaneReference {
            override fun isValidForLane(laneNumber: Int): Boolean = laneNumber == laneNumberValidity
        }

    fun createRangeLaneReference(
        startLaneNumber: Int,
        endLaneNumber: Int,
    ): LaneReference =
        object : LaneReference {
            override fun isValidForLane(laneNumber: Int): Boolean = laneNumber in startLaneNumber..endLaneNumber
        }

    fun createFromLaneReference(fromLaneNumber: Int): LaneReference = createRangeLaneReference(fromLaneNumber, Int.MAX_VALUE)

    fun createToLaneReference(toLaneNumber: Int): LaneReference = createRangeLaneReference(1, toLaneNumber)

    fun createAllLaneReference(): LaneReference =
        object : LaneReference {
            override fun isValidForLane(laneNumber: Int): Boolean = true
        }
}
