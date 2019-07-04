package com.ximedes.vas.util

import com.ximedes.dynamodb.dsl.builders.CreateTableRequestBuilder
import com.ximedes.dynamodb.dsl.createTable
import com.ximedes.dynamodb.dsl.describeTable
import kotlinx.coroutines.delay
import mu.KotlinLogging
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException
import software.amazon.awssdk.services.dynamodb.model.TableStatus

fun DynamoDbClient.assertTable(
    tableName: String,
    init: CreateTableRequestBuilder.() -> Unit
) {
    val logger = KotlinLogging.logger {}
    var delayMS = 250L
    do {
        val status = try {
            val describeTableResponse = describeTable(tableName)
            describeTableResponse.table().tableStatus()
        } catch (e: ResourceNotFoundException) {
            logger.info { "Table $tableName does not exist, creating it now" }
            createTable(tableName, init)
            TableStatus.CREATING
        }
        if (status != TableStatus.ACTIVE) {
            delayMS = (2 * delayMS).coerceAtMost(4000L)
            logger.info { "Table $tableName is in status $status, checking again in $delayMS ms" }
            Thread.sleep(delayMS)
        }

    } while (status != TableStatus.ACTIVE)
    logger.info { "Table $tableName is ACTIVE" }

}

suspend fun DynamoDbAsyncClient.assertTable(
    tableName: String,
    init: CreateTableRequestBuilder.() -> Unit
) {
    val logger = KotlinLogging.logger {}
    var delayMS = 250L
    do {
        val status = try {
            val describeTableResponse = describeTable(tableName)
            describeTableResponse.table().tableStatus()
        } catch (e: ResourceNotFoundException) {
            logger.info { "Table $tableName does not exist, creating it now" }
            createTable(tableName, init)
            TableStatus.CREATING
        }
        if (status != TableStatus.ACTIVE) {
            logger.info { "Table $tableName is in status $status, checking again later" }
            delay(delayMS)
            delayMS = (2 * delayMS).coerceAtMost(4000L)
        }

    } while (status != TableStatus.ACTIVE)

}
