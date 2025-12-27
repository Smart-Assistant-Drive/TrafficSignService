package com.example.rest.domainLayer

import kotlin.properties.Delegates

interface TrafficSign {
    val roadId: String
    val directionId: Int
    val geoLocationPosition: GeoLocationPosition
    val trafficSignType: TrafficSignType
    // val integrationPanel: List<IntegrationPanel>,

    private data class TrafficSignImpl(
        override val roadId: String,
        override val directionId: Int,
        override val geoLocationPosition: GeoLocationPosition,
        override val trafficSignType: TrafficSignType,
        // override val integrationPanel: List<IntegrationPanel>,
    ) : TrafficSign

    class Builder {
        private var roadId by Delegates.notNull<String>()
        private var directionId by Delegates.notNull<Int>()
        private lateinit var geoLocationPosition: GeoLocationPosition
        private lateinit var trafficSignType: TrafficSignType
        // private var integrationPanel: List<IntegrationPanel> = emptyList()

        fun roadId(roadId: String) = apply { this.roadId = roadId }

        fun directionId(directionId: Int) = apply { this.directionId = directionId }

        fun geoLocationPosition(geoLocationPosition: GeoLocationPosition) = apply { this.geoLocationPosition = geoLocationPosition }

        fun trafficSignType(trafficSignType: TrafficSignType) = apply { this.trafficSignType = trafficSignType }
        // fun integrationPanel(integrationPanel: List<IntegrationPanel>) = apply { this.integrationPanel = integrationPanel }

        fun build(): TrafficSign = TrafficSignImpl(roadId, directionId, geoLocationPosition, trafficSignType)
    }
}
