package com.example.rest.domainLayer

/**
 * Interface for a reference of a traffic sign to a lane.
 */
interface LaneReference {
    /**
     * Returns true if the reference is valid for the given lane number.
     * @param laneNumber the lane number to check from 1 to the number of lanes (inclusive).
     * The lane number is 1-based and is calculated from the most external lane to the most internal lane.
     * Example: a road with 3 lanes has lane numbers 1, 2, 3.
     * | 1 | 2 | 3 |separation| 3 | 2 | 1 |
     *
     * @return true if the reference is valid for the given lane number.
     *
     */
    fun isValidForLane(laneNumber: Int): Boolean
}
