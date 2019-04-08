package com.ximedes.vas.dsl

import com.ximedes.vas.dsl.builders.CreateTableRequestBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.future.await
import mu.KotlinLogging
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException
import software.amazon.awssdk.services.dynamodb.model.TableStatus

fun DynamoDbClient.createTable(
    tableName: String,
    init: CreateTableRequestBuilder.() -> Unit
): CreateTableResponse {
    val builder = CreateTableRequestBuilder(tableName)
    builder.init()
    val request = builder.build()
    return createTable(request)
}

suspend fun DynamoDbAsyncClient.createTable(
    tableName: String,
    init: CreateTableRequestBuilder.() -> Unit
): CreateTableResponse {
    val builder = CreateTableRequestBuilder(tableName)
    builder.init()
    val request = builder.build()
    return createTable(request).await()
}

suspend fun DynamoDbAsyncClient.assertTable(
    tableName: String,
    init: CreateTableRequestBuilder.() -> Unit
) {
    val logger = KotlinLogging.logger {}
    do {
        val status = try {
            val tableDescription = describeTable(DescribeTableRequest.builder().tableName(tableName).build()).await()
            tableDescription.table().tableStatus()
        } catch (e: ResourceNotFoundException) {
            logger.info { "Table $tableName does not exit, creating it now" }
            createTable(tableName, init)
            TableStatus.CREATING
        }
        if (status != TableStatus.ACTIVE) {
            logger.info { "Table $tableName is in status $status, checking again later" }
            delay(500)
        }

    } while (status != TableStatus.ACTIVE)

}

