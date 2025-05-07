package com.example.rest.health

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Component

@Component("db")
@ConditionalOnEnabledHealthIndicator("db")
class HealthCheck(
    val mongoTemplate: MongoTemplate,
) : HealthIndicator {
    var logger: Logger = LoggerFactory.getLogger(HealthCheck::class.java)

    override fun health(): Health {
        val errorCode = check() // perform some specific health check
        if (errorCode != 0) {
            return Health
                .down()
                .build()
        }
        return Health
            .up()
            .build()
    }

    fun check(): Int {
        try {
            val result = mongoTemplate.executeCommand("{ connectionStatus: 1 }")
            logger.info("result: $result")
            return 0
        } catch (e: Exception) {
            logger.error("Error: ${e.message}")
            return 1
        }
    }
}
